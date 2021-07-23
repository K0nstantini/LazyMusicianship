package com.grommade.lazymusicianship.ui_practice_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.*
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.domain.repos.RepoSection
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeDetailsViewModel @Inject constructor(
    private val repoPractice: RepoPractice,
    private val repoSection: RepoSection,
    repoPiece: RepoPiece,
    repoStateStudy: RepoStateStudy,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PracticeDetailsActions>()

    private val currentPracticeItem = MutableStateFlow(PracticeWithPieceAndSections())

    private val allPieces = repoPiece.getPiecesFlow()
    private val allSections = currentPracticeItem.flatMapLatest { pieceItem ->
        repoSection.getSectionsFlow(pieceItem.piece.id)
    }
    private val allStates = repoStateStudy.getStatesFlow()

    val state = combine(
        currentPracticeItem,
        allPieces,
        allSections,
        allStates
    ) { practiceItem, pieces, sections, states ->
        PracticeDetailsViewState(
            practiceItem = practiceItem,
            allPieces = pieces,
            allSections = sections,
            allStates = states,
            errorSections = sectionsNotCorrect()
        )
    }

    // FIXME
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            initPractice()
            pendingActions.collect { action ->
                when (action) {
                    is PracticeDetailsActions.ChangeDate -> changePractice { copy(date = action.date) }
                    is PracticeDetailsActions.ChangePiece -> changePiece(action.piece)
                    is PracticeDetailsActions.ChangeSectionFrom -> changeSectionFrom(action.section)
                    is PracticeDetailsActions.ChangeSectionTo -> changeSectionTo(action.section)
                    is PracticeDetailsActions.ChangeState -> changeStateStudy(action.state)
                    PracticeDetailsActions.SaveAndClose -> save()
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initPractice() {
        val practiceId = handle.get<Long>(Keys.PRACTICE_ID) ?: 0
        repoPractice.getPracticeItem(practiceId)?.let { practiceItem ->
            currentPracticeItem.value = practiceItem
        }
    }

    private fun changePiece(piece: Piece) {
        changePractice { copy(pieceId = piece.id, sectionIdFrom = 0, sectionIdTo = 0) }
        changePracticeItem { copy(piece = piece, sectionFrom = null, sectionTo = null) }
    }

    private fun changeSectionFrom(section: Section) {
        changePractice { copy(sectionIdFrom = section.id) }
        changePracticeItem { copy(sectionFrom = section) }
        if (currentPracticeItem.value.sectionTo == null) {
            changePractice { copy(sectionIdTo = section.id) }
            changePracticeItem { copy(sectionTo = section) }
        }
    }

    private fun changeSectionTo(section: Section) {
        changePractice { copy(sectionIdTo = section.id) }
        changePracticeItem { copy(sectionTo = section) }
    }

    private fun changeStateStudy(state: StateStudy) {
        changePractice { copy(stateId = state.id) }
        changePracticeItem { copy(stateStudy = state) }
    }

    private fun changePractice(foo: Practice.() -> Practice) {
        val practice = foo(currentPracticeItem.value.practice)
        changePracticeItem { copy(practice = practice) }
    }

    private fun changePracticeItem(foo: PracticeWithPieceAndSections.() -> PracticeWithPieceAndSections) {
        currentPracticeItem.value = foo(currentPracticeItem.value)
    }

    private fun save() {
        viewModelScope.launch {
            repoPractice.save(currentPracticeItem.value.practice)
            navigateToBack.emit(true)
        }
    }

    private fun sectionsNotCorrect(): Boolean {
        val sectionFrom = currentPracticeItem.value.sectionFrom ?: Section()
        val sectionTo = currentPracticeItem.value.sectionTo ?: Section()
        val oneEmpty = sectionFrom.isNew xor sectionTo.isNew
        val diffParent = sectionFrom.parentId != sectionTo.parentId
        val lastMoreFirst = sectionFrom.order > sectionTo.order
        return oneEmpty || diffParent || lastMoreFirst
    }

    fun submitAction(action: PracticeDetailsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}