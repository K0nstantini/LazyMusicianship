package com.grommade.lazymusicianship.ui_practice_details

import androidx.compose.ui.state.ToggleableState
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

    private val stateSections = MutableStateFlow(mapOf<Section, ToggleableState>())

    val state = combine(
        currentPracticeItem,
        observePieces.observe(),
        stateSections,
        allSections,
        observeStates.observe()
    ) { practiceItem, pieces, selected, sections, states ->
        PracticeDetailsViewState(
            practiceItem = practiceItem,
            selectedSections = selected,
            allPieces = pieces,
            allSections = sections,
            allStates = states,
            saveEnabled = with(practiceItem) { !(piece.isNew || stateStudy.isNew) }
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

    private fun selectSection(section: Section) {
        val newState = when (stateSections.value.getOrDefault(section, ToggleableState.Off)) {
            ToggleableState.Off -> ToggleableState.On
            else -> ToggleableState.Off
        }
        stateSections.value = stateSections.value.filterKeys { it != section } + (section to newState)
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

    fun submitAction(action: PracticeDetailsActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}