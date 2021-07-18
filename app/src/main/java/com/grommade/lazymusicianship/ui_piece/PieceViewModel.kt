package com.grommade.lazymusicianship.ui_piece

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.DataTransfer
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.repos.RepoDataTransfer
import com.grommade.lazymusicianship.data.repos.RepoPiece
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PieceViewModel @Inject constructor(
    private val repoPiece: RepoPiece,
    private val repoDataTransfer: RepoDataTransfer,
    handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PieceActions>()

    private val pieceId: Long = handle.get<Long>(Keys.PIECE_ID) ?: -1L
    private val currentPiece = MutableStateFlow(PieceWithSections(Piece(), emptyList()))

    private val selectedSection = MutableStateFlow(-1)

    val state = combine(currentPiece, selectedSection) { piece, selected ->
        PieceViewState(
            piece = piece.piece,
            sections = piece.sections.sortedBy { section -> section.order },
            selectedSection = selected
        )
    }

    // FIXME
    val navigateToSection = MutableSharedFlow<Boolean>()
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
                    is PieceActions.OpenSection -> openSection(action.order)
                    is PieceActions.SelectSection -> selectSection(action.order)
                    is PieceActions.DeleteSection -> deleteSection(action.order)
                    PieceActions.Save -> save()
                    else -> {
                    }
                }
            }
        }
    }

    private fun changeName(value: String) {
        changePiece { currentPiece.value.piece.copy(name = value) }
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

    private fun openSection(order: Int) {
        selectSection(-1)
        viewModelScope.launch {
            currentPiece.value.sections.find { it.order == order }?.let { section ->
                repoDataTransfer.save(DataTransfer(section = section))
            }
            navigateToSection.emit(true)
        }
    }

    private fun selectSection(order: Int) {
        selectedSection.value = order
    }

    private fun deleteSection(order: Int) {
        val sections = currentPiece.value.sections.filter { it.order != order }
        currentPiece.value = currentPiece.value.copy(sections = sections)
    }

    private fun save() {
        viewModelScope.launch {
            repoPiece.save(currentPiece.value)
            navigateToBack.emit(true)
        }
    }

    fun refreshSections(section: Section) {
        with(currentPiece.value) {
            val order = when {
                section.order >= 0 -> section.order
                sections.isEmpty() -> 0
                else -> sections.maxOf { it.order } + 1
            }
            val sections = sections.filter { it.order != section.order } + section.copy(order = order)
            currentPiece.value = copy(sections = sections)
        }
    }

    fun submitAction(action: PieceActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}