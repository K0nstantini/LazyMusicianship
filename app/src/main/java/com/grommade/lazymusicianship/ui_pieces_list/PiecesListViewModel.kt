package com.grommade.lazymusicianship.ui_pieces_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.repos.RepoPiece
import com.grommade.lazymusicianship.use_cases.PopulateDBWithPieces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PiecesListViewModel @Inject constructor(
    repoPiece: RepoPiece,
    populateDBWithPieces: PopulateDBWithPieces
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PiecesListActions>()

    private val piecesList = repoPiece.piecesFlow

    val state = piecesList.map { pieces ->
        PiecesListViewState(pieces = pieces)
    }

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    PiecesListActions.PopulateDB -> populateDBWithPieces()
                    else -> {
                    }
                }
            }
        }
    }

    fun submitAction(action: PiecesListActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}