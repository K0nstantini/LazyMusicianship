package com.grommade.lazymusicianship.ui_section

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.repos.RepoSection
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val repoSection: RepoSection,
    handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<SectionActions>()

    private val pieceId = handle.get<Long>(Keys.PIECE_ID) ?: 0
    private val parentId = handle.get<Long>(Keys.PARENT_ID) ?: 0
    private val sectionId: Long = handle.get<Long>(Keys.SECTION_ID) ?: 0

    private val currentSection = MutableStateFlow(Section())

    val state = currentSection.map { section ->
        SectionViewState(section = section)
    }

    // FIXME
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            initSection()
            pendingActions.collect { action ->
                when (action) {
                    is SectionActions.ChangeName -> changeName(action.value)
                    is SectionActions.ChangeTempo -> changeTempo(action.value)
                    is SectionActions.ChangeNew -> changeNew(action.value)
                    SectionActions.Save -> save()
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initSection() {
        currentSection.value = when (val section = repoSection.getSection(sectionId)) {
            is Section -> section
            else -> createNewSection()
        }
    }

    private suspend fun createNewSection(): Section {
        val lastSection = repoSection.getLastCreated(pieceId) ?: Section()
        return Section(
            pieceId = pieceId,
            parentId = parentId,
            tempo = lastSection.tempo,
            firstTime = lastSection.firstTime
        )
    }


    private fun changeName(value: String) {
        changeSection { copy(name = value) }
    }

    private fun changeTempo(value: String) {
        val beat = value.toIntOrNull() ?: 0
        changeSection { copy(tempo = beat) }
    }

    private fun changeNew(value: Boolean) {
        changeSection { copy(firstTime = value) }
    }

    private fun changeSection(foo: Section.() -> Section) {
        currentSection.value = foo(currentSection.value)
    }

    private fun save() {
        viewModelScope.launch {
            repoSection.save(currentSection.value)
            navigateToBack.emit(true)
        }
    }


    fun submitAction(action: SectionActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}