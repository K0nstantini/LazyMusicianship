package com.grommade.lazymusicianship.ui_section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.repos.RepoDataTransfer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val repoDataTransfer: RepoDataTransfer,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<SectionActions>()

    private val currentSection = MutableStateFlow(Section())

    val state = currentSection.map { section ->
        SectionViewState(section = section)
    }

    // FIXME
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            repoDataTransfer.getData()?.let { data ->
                currentSection.value = data.section
            }
            pendingActions.collect { action ->
                when (action) {
                    is SectionActions.ChangeName -> changeName(action.value)
                    is SectionActions.ChangeBeat -> changeBeat(action.value)
                    is SectionActions.ChangeBars -> changeBars(action.value)
                    is SectionActions.ChangeNew -> changeNew(action.value)
                    else -> {
                    }
                }
            }
        }
    }

    private fun changeName(value: String) {
        changeSection { copy(name = value) }
    }

    private fun changeBeat(value: String) {
        val beat = value.toIntOrNull() ?: 0
        changeSection { copy(beat = beat) }
    }

    private fun changeBars(value: String) {
        val countBars = value.toIntOrNull() ?: 0
        changeSection { copy(countBars = countBars) }
    }

    private fun changeNew(value: Boolean) {
        changeSection { copy(isNew = value) }
    }

    private fun changeSection(foo: Section.() -> Section) {
        currentSection.value = foo(currentSection.value)
    }


    fun submitAction(action: SectionActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}