package com.grommade.lazymusicianship.ui_pieces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.domain.use_cases.PopulateDBWithPieces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PiecesListViewModel @Inject constructor(
    private val repoPiece: RepoPiece,
    populateDBWithPieces: PopulateDBWithPieces
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PiecesListActions>()

    private val piecesList = repoPiece.getPiecesFlow()

    private val selectedPiece = MutableStateFlow(0L)

    val state = combine(piecesList, selectedPiece) { pieces, selected ->
        PiecesListViewState(
            pieces = pieces,
            selectedPiece = selected
        )
    }

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is PiecesListActions.SelectPiece -> selectPiece(action.id)
                    is PiecesListActions.Delete -> delete(action.piece)
                    PiecesListActions.PopulateDB -> populateDBWithPieces()
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
            repoPiece.delete(piece)
        }
    }

    fun submitAction(action: PiecesListActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}