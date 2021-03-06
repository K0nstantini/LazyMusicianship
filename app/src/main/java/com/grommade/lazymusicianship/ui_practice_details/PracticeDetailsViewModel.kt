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
import com.grommade.lazymusicianship.util.extentions.combine
import com.grommade.lazymusicianship.util.extentions.sameLevel
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

    private val selectedPiece = MutableStateFlow(false)
    private val selectedSections = MutableStateFlow(emptyList<Section>())

    val state = combine(
        currentPracticeItem,
        observePieces.observe(),
        selectedPiece,
        selectedSections,
        allSections,
        observeStates.observe()
    ) { practiceItem, pieces, sPiece, sSections, sections, states ->
        PracticeDetailsViewState(
            practiceItem = practiceItem,
            selectedPiece = sPiece,
            selectedSections = sSections,
            allPieces = pieces,
            allSections = sections,
            allStates = states,
            saveEnabled = saveEnabled()
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
                    is PracticeDetailsActions.SelectSection -> selectSection(action.section)
                    is PracticeDetailsActions.ChangeTime -> changePractice { copy(elapsedTime = action.value) }
                    is PracticeDetailsActions.ChangeState -> changeStateStudy(action.state)
                    is PracticeDetailsActions.ChangeTempo -> changePractice { copy(tempo = action.value) }
                    is PracticeDetailsActions.ChangeNumberTimes -> changePractice { copy(countTimes = action.value) }
                    PracticeDetailsActions.SelectPiece -> selectPiece()
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initPractice() {
        val practiceId = handle.get<Long>(Keys.PRACTICE_ID) ?: 0
        getPractice(GetPractice.Params(practiceId)).first()
            .doIfSuccess { item ->
                currentPracticeItem.value = item
                selectedPiece.value = item.sectionFrom == null
                if (item.sectionFrom != null && item.sectionTo != null) {
                    val sections = allSections.first()
                        .filter { it.parentId == item.sectionFrom.parentId }
                        .sortedBy { it.order }
                    selectedSections.value = sections
                        .filter { it.order in item.sectionFrom.order..item.sectionTo.order }
                }
            }
    }

    private fun changePiece(piece: Piece) {
        changePractice { copy(pieceId = piece.id, sectionIdFrom = 0, sectionIdTo = 0) }
        changePracticeItem { copy(piece = piece, sectionFrom = null, sectionTo = null) }
    }

    private fun selectSection(section: Section) {
        val sections = selectedSections.value
        if (sections.contains(section)) {
            selectedSections.value = when (val remainingSections = sections - section) {
                emptyList<Section>() -> emptyList()
                else -> remainingSections.filterSections(remainingSections.first())
            }
        } else {
            selectedSections.value = sections.filterSections(section) + section
            selectedPiece.value = false
        }
        val sortedSections = selectedSections.value.sortedBy { it.order }
        if (sortedSections.isEmpty()) {
            changePractice { copy(sectionIdFrom = 0L, sectionIdTo = 0L) }
        } else {
            changePractice { copy(sectionIdFrom = sortedSections.first().id, sectionIdTo = sortedSections.last().id) }
        }
    }

    private fun List<Section>.filterSections(section: Section): List<Section> {
        val filtered = filter { it.parentId == section.parentId }

        val sectionsAfter = generateSequence(section) { s -> filtered.find { it.order == s.order + 1 } }
        val sectionsBefore = generateSequence(section) { s -> filtered.find { it.order == s.order - 1 } }
        val sections = (sectionsAfter + sectionsBefore).toList()
        return this.filter { sections.contains(it) }
    }

    private fun selectPiece() {
        selectedPiece.value = !selectedPiece.value
        selectedSections.value = emptyList()
    }

    private fun changeStateStudy(state: StateStudy) {
        changePractice { copy(stateId = state.id) }
        changePracticeItem { copy(stateStudy = state) }
    }

    private fun changePractice(foo: Practice.() -> Practice) {
        val practice = foo(currentPracticeItem.value.practice)
        changePracticeItem { copy(practice = practice) }
    }

    private fun saveEnabled(): Boolean {
        val filledFields = with(currentPracticeItem.value) { !piece.isNew && !stateStudy.isNew }
        val selectedPieceOrSection = selectedSections.value.isNotEmpty() xor selectedPiece.value
        return filledFields && selectedPieceOrSection && selectedSections.value.sameLevel() // fixme
    }

    private fun changePracticeItem(foo: PracticeWithPieceAndSections.() -> PracticeWithPieceAndSections) {
        currentPracticeItem.value = foo(currentPracticeItem.value)
    }

    fun save() = CoroutineScope(Job()).launch {
        savePractice(SavePractice.Params(currentPracticeItem.value.practice)).collect()
    }

    fun submitAction(action: PracticeDetailsActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}