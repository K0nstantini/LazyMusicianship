package com.grommade.lazymusicianship.ui_practice_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.*
import com.grommade.lazymusicianship.domain.observers.ObservePieces
import com.grommade.lazymusicianship.domain.observers.ObserveSections
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.use_cases.GetPractice
import com.grommade.lazymusicianship.domain.use_cases.SavePractice
import com.grommade.lazymusicianship.util.Keys
import com.grommade.lazymusicianship.util.doIfSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeDetailsViewModel @Inject constructor(
    private val getPractice: GetPractice,
    private val savePractice: SavePractice,
    private val handle: SavedStateHandle,
    observePieces: ObservePieces,
    observeSections: ObserveSections,
    observeStates: ObserveStates,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PracticeDetailsActions>()

    private val currentPracticeItem = MutableStateFlow(PracticeWithPieceAndSections())

    private val allSections = currentPracticeItem.flatMapLatest { pieceItem ->
        observeSections(ObserveSections.Params(pieceItem.piece.id))
        observeSections.observe()
    }

    val state = combine(
        currentPracticeItem,
        observePieces.observe(),
        allSections,
        observeStates.observe()
    ) { practiceItem, pieces, sections, states ->
        PracticeDetailsViewState(
            practiceItem = practiceItem,
            allPieces = pieces,
            allSections = sections,
            allStates = states,
            errorSections = sectionsNotCorrect()
        )
    }

    init {
        observePieces(Unit)
        observeStates(Unit)

        viewModelScope.launch {
            initPractice()
            pendingActions.collect { action ->
                when (action) {
                    is PracticeDetailsActions.ChangeDate -> changePractice { copy(date = action.date) }
                    is PracticeDetailsActions.ChangePiece -> changePiece(action.piece)
                    is PracticeDetailsActions.ChangeSectionFrom -> changeSectionFrom(action.section)
                    is PracticeDetailsActions.ChangeSectionTo -> changeSectionTo(action.section)
                    is PracticeDetailsActions.ChangeTime -> changePractice { copy(elapsedTime = action.value) }
                    is PracticeDetailsActions.ChangeSuccessful -> changePractice { copy(successful = action.value) }
                    is PracticeDetailsActions.ChangeState -> changeStateStudy(action.state)
                    is PracticeDetailsActions.ChangeTempo -> changePractice { copy(tempo = action.value) }
                    is PracticeDetailsActions.ChangeCountTimes -> changePractice { copy(countTimes = action.value) }
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initPractice() {
        val practiceId = handle.get<Long>(Keys.PRACTICE_ID) ?: 0
        getPractice(GetPractice.Params(practiceId)).first()
            .doIfSuccess { currentPracticeItem.value = it }
    }

    private fun changePiece(piece: Piece) {
        changePractice { copy(pieceId = piece.id, sectionIdFrom = 0, sectionIdTo = 0) }
        changePracticeItem { copy(piece = piece, sectionFrom = null, sectionTo = null) }
    }

    private fun changeSectionFrom(section: Section) {
        changePractice { copy(sectionIdFrom = section.id) }
        changePracticeItem { copy(sectionFrom = section) }
        if (currentPracticeItem.value.sectionTo == null) {
            changeSectionTo(section)
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

    fun save() = CoroutineScope(Job()).launch {
        savePractice(SavePractice.Params(currentPracticeItem.value.practice)).collect()
    }

    private fun sectionsNotCorrect(): Boolean {
        val sectionFrom = currentPracticeItem.value.sectionFrom ?: Section()
        val sectionTo = currentPracticeItem.value.sectionTo ?: Section()
        val oneEmpty = sectionFrom.isNew xor sectionTo.isNew
        val diffParent = sectionFrom.parentId != sectionTo.parentId
        val lastMoreFirst = sectionFrom.order > sectionTo.order
        return oneEmpty || diffParent || lastMoreFirst
    }

    fun submitAction(action: PracticeDetailsActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}