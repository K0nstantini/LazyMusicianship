package com.grommade.lazymusicianship.ui_pieces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.observers.ObservePieces
import com.grommade.lazymusicianship.domain.use_cases.DeletePiece
import com.grommade.lazymusicianship.domain.use_cases.PopulateDBWithPiecesImpl
import com.grommade.lazymusicianship.ui.common.SnackBarManager
import com.grommade.lazymusicianship.util.doIfFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PiecesListViewModel @Inject constructor(
    private val deletePiece: DeletePiece,
    private val snackBarManager: SnackBarManager,
    observePieces: ObservePieces,
    populateDBWithPieces: PopulateDBWithPiecesImpl
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PiecesListActions>()

    private val selectedPiece = MutableStateFlow(0L)

    val state = combine(
        observePieces.observe(),
        selectedPiece,
        snackBarManager.errors
    ) { pieces, selected, error ->
        PiecesListViewState(
            pieces = pieces,
            selectedPiece = selected,
            error = error
        )
    }

    init {
        observePieces(Unit)

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is PiecesListActions.SelectPiece -> selectPiece(action.id)
                    is PiecesListActions.Delete -> delete(action.piece)
                    PiecesListActions.PopulateDB -> populateDBWithPieces()
                    PiecesListActions.ClearError -> snackBarManager.removeCurrentError()
                    else -> {
                    }
                }
            }
        }
    }

    private fun selectPiece(id: Long) {
        selectedPiece.value = id
    }

    private fun delete(piece: Piece) {
        viewModelScope.launch {
            deletePiece(DeletePiece.Params(piece)).first()
                .doIfFailure { message, _ ->
                    snackBarManager.addError(message ?: "Unknown error message")
                }
        }
    }

    fun submitAction(action: PiecesListActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}