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
    repoPiece: RepoPiece,
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

    init {
        viewModelScope.launch {
            repoPiece.getPieceWithSections(pieceId)?.let { piece ->
                currentPiece.value = piece
            }
            pendingActions.collect { action ->
                when (action) {
                    PieceActions.Save -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun submitAction(action: PieceActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}