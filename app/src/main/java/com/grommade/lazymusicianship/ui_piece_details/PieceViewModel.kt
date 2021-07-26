package com.grommade.lazymusicianship.ui_piece_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.observers.ObserveSections
import com.grommade.lazymusicianship.domain.use_cases.DeleteSection
import com.grommade.lazymusicianship.domain.use_cases.GetPiece
import com.grommade.lazymusicianship.domain.use_cases.SavePiece
import com.grommade.lazymusicianship.ui.common.SnackBarManager
import com.grommade.lazymusicianship.util.Keys
import com.grommade.lazymusicianship.util.doIfFailure
import com.grommade.lazymusicianship.util.doIfSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PieceViewModel @Inject constructor(
    private val getPiece: GetPiece,
    private val savePiece: SavePiece,
    private val deleteSection: DeleteSection,
    private val snackBarManager: SnackBarManager,
    private val handle: SavedStateHandle,
    observeSections: ObserveSections
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PieceActions>()

    private val currentPiece = MutableStateFlow(Piece())

    private val currentSections = currentPiece.flatMapLatest { piece ->
        observeSections(ObserveSections.Params(piece.id))
        observeSections.observe()
    }

    private val selectedSection = MutableStateFlow(0L)

    val state = combine(
        currentPiece,
        currentSections,
        selectedSection,
        snackBarManager.errors
    ) { piece, sections, selected, error ->
        PieceViewState(
            piece = piece,
            sections = sections.sortedBy { it.hierarchicalSort(sections) },
            selectedSection = selected,
            error = error
        )
    }

    init {
        viewModelScope.launch {
            initPiece()

            pendingActions.collect { action ->
                when (action) {
                    is PieceActions.ChangeName -> changeName(action.value)
                    is PieceActions.ChangeAuthor -> changeAuthor(action.value)
                    is PieceActions.ChangeArranger -> changeArranger(action.value)
                    is PieceActions.ChangeTime -> changeTime(action.value)
                    is PieceActions.ChangeDescription -> changeDescription(action.value)
                    is PieceActions.SelectSection -> selectSection(action.id)
                    is PieceActions.DeleteSection -> deleteSection(action.section)
                    PieceActions.ClearError -> snackBarManager.removeCurrentError()
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initPiece() {
        val pieceId = handle.get<Long>(Keys.PIECE_ID) ?: 0
        getPiece(GetPiece.Params(pieceId)).first()
            .doIfSuccess { currentPiece.value = it }
    }

    private fun changeName(value: String) {
        changePiece { currentPiece.value.copy(name = value) }
    }

    private fun changeAuthor(value: String) {
        changePiece { currentPiece.value.copy(author = value) }
    }

    private fun changeArranger(value: String) {
        changePiece { currentPiece.value.copy(arranger = value) }
    }

    private fun changeTime(value: Int) {
        changePiece { currentPiece.value.copy(time = value) }
    }

    private fun changeDescription(value: String) {
        changePiece { currentPiece.value.copy(description = value) }
    }

    private fun selectSection(id: Long) {
        selectedSection.value = id
    }

    private fun deleteSection(section: Section) {
        viewModelScope.launch {
            deleteSection(DeleteSection.Params(section)).first()
                .doIfFailure { message, _ ->
                    snackBarManager.addError(message ?: "Unknown error message")
                }
        }
    }

    private fun changePiece(function: () -> Piece) {
        currentPiece.value = function()
    }

    suspend fun save(): Long {
        val id = savePiece(SavePiece.Params(currentPiece.value)).first()
        changePiece { currentPiece.value.copy(id = id) }
        return id
    }

    fun submitAction(action: PieceActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}