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
                    is PieceActions.ChangeName -> changePiece { copy(name = action.value) }
                    is PieceActions.ChangeAuthor -> changePiece { copy(author = action.value) }
                    is PieceActions.ChangeArranger -> changePiece { copy(arranger = action.value) }
                    is PieceActions.ChangeTime -> changePiece { copy(time = action.value) }
                    is PieceActions.ChangeDescription -> changePiece { copy(description = action.value) }
                    is PieceActions.SelectSection -> selectedSection.value = action.id
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

    private fun deleteSection(section: Section) = viewModelScope.launch {
        deleteSection(DeleteSection.Params(section)).first()
            .doIfFailure { message, _ ->
                snackBarManager.addError(message ?: "Unknown error message")
            }
    }

    private fun changePiece(foo: Piece.() -> Piece) {
        currentPiece.value = foo(currentPiece.value)
    }

    suspend fun save(): Long {
        val id = savePiece(SavePiece.Params(currentPiece.value)).first()
        changePiece { currentPiece.value.copy(id = id) }
        return id
    }

    fun submitAction(action: PieceActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}