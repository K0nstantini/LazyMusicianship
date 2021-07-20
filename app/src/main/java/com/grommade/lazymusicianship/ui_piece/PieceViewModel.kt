package com.grommade.lazymusicianship.ui_piece

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.repos.RepoPiece
import com.grommade.lazymusicianship.data.repos.RepoSection
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PieceViewModel @Inject constructor(
    private val repoPiece: RepoPiece,
    private val repoSection: RepoSection,
    handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PieceActions>()

    private val pieceId: Long = handle.get<Long>(Keys.PIECE_ID) ?: -1L
    private val currentPiece = MutableStateFlow(Piece())

    @ExperimentalCoroutinesApi
    private val currentSections = currentPiece.flatMapLatest {
        repoSection.getSectionsFlow(currentPiece.value.id)
    }

    private val selectedSection = MutableStateFlow(0L)

    @ExperimentalCoroutinesApi
    val state = combine(currentPiece, currentSections, selectedSection) { piece, sections, selected ->
        PieceViewState(
            piece = piece,
            sections = sections.sortedBy { it.hierarchicalSort(sections) },
            selectedSection = selected
        )
    }

    val navigateToSection = MutableSharedFlow<Long>()
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            repoPiece.getPiece(pieceId)?.let { piece ->
                currentPiece.value = piece
            }
            pendingActions.collect { action ->
                when (action) {
                    is PieceActions.ChangeName -> changeName(action.value)
                    is PieceActions.ChangeAuthor -> changeAuthor(action.value)
                    is PieceActions.ChangeArranger -> changeArranger(action.value)
                    is PieceActions.ChangeTempo -> changeBeat(action.value)
                    is PieceActions.ChangeTime -> changeTime(action.value)
                    is PieceActions.SelectSection -> selectSection(action.id)
                    is PieceActions.DeleteSection -> deleteSection(action.section)
                    PieceActions.SaveAndClose -> saveAndClose()
                    else -> {
                    }
                }
            }
        }
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

    private fun changeBeat(value: String) {
        val beat = value.toIntOrNull() ?: 0
        changePiece { currentPiece.value.copy(tempo = beat) }
    }

    private fun changeTime(value: Int) {
        changePiece { currentPiece.value.copy(time = value) }
    }

    private fun selectSection(id: Long) {
        selectedSection.value = id
    }

    private fun deleteSection(section: Section) {
        viewModelScope.launch {
            repoSection.delete(section)
        }
    }

    private fun changePiece(function: () -> Piece) {
        currentPiece.value = function()
    }

    private fun saveAndClose() {
        viewModelScope.launch {
            repoPiece.save(currentPiece.value)
            navigateToBack.emit(true)
        }
    }

    fun saveAndAddSection() {
        viewModelScope.launch {
            val id = repoPiece.save(currentPiece.value)
            changePiece { currentPiece.value.copy(id = id) }
            navigateToSection.emit(id)
        }
    }

    fun submitAction(action: PieceActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}