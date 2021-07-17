package com.grommade.lazymusicianship.ui_piece

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.repos.RepoPiece
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PieceViewModel @Inject constructor(
    private val repoPiece: RepoPiece,
    handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PieceActions>()

    private val pieceId: Long = handle.get<Long>(Keys.PIECE_ID) ?: -1L
    private val currentPiece = MutableStateFlow(PieceWithSections(Piece(), emptyList()))

    val state = currentPiece.map {
        PieceViewState(
            piece = it.piece,
            sections = it.sections
        )
    }

    // FIXME
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            repoPiece.getPieceWithSections(pieceId)?.let { piece ->
                currentPiece.value = piece
            }
            pendingActions.collect { action ->
                when (action) {
                    is PieceActions.ChangeName -> changeName(action.value)
                    is PieceActions.ChangeAuthor -> changeAuthor(action.value)
                    is PieceActions.ChangeArranger -> changeArranger(action.value)
                    PieceActions.Save -> save()
                    else -> {
                    }
                }
            }
        }
    }

    private fun changeName(value: String) {
        changePiece { currentPiece.value.piece.copy(title = value) }
    }

    private fun changeAuthor(value: String) {
        changePiece { currentPiece.value.piece.copy(author = value) }
    }

    private fun changeArranger(value: String) {
        changePiece { currentPiece.value.piece.copy(arranger = value) }
    }

    private fun changePiece(function: () -> Piece) {
        currentPiece.value = currentPiece.value.copy(piece = function())
    }

    private fun save() {
        viewModelScope.launch {
            repoPiece.save(currentPiece.value)
            navigateToBack.emit(true)
        }
    }

    fun submitAction(action: PieceActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}