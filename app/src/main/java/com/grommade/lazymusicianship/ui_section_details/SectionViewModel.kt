package com.grommade.lazymusicianship.ui_section_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.use_cases.GetSection
import com.grommade.lazymusicianship.domain.use_cases.NewSection
import com.grommade.lazymusicianship.domain.use_cases.SaveSection
import com.grommade.lazymusicianship.util.Keys
import com.grommade.lazymusicianship.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val getSection: GetSection,
    private val newSection: NewSection,
    private val saveSection: SaveSection,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<SectionActions>()

    private val currentSection = MutableStateFlow(Section())

    val state = currentSection.map { section ->
        SectionViewState(section = section)
    }

    init {
        viewModelScope.launch {
            initSection()

            pendingActions.collect { action ->
                when (action) {
                    is SectionActions.ChangeName -> changeSection { copy(name = action.value) }
                    is SectionActions.ChangeTempo -> changeSection { copy(tempo = action.value) }
                    is SectionActions.ChangeNew -> changeSection { copy(firstTime = action.value) }
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initSection() {
        val sectionId = handle.get<Long>(Keys.SECTION_ID) ?: 0
        val pieceId = handle.get<Long>(Keys.PIECE_ID) ?: 0
        val parentId = handle.get<Long>(Keys.PARENT_ID) ?: 0

        val result = getSection(GetSection.Params(sectionId)).first()
        currentSection.value = when (result) {
            is ResultOf.Success -> result.value
            is ResultOf.Failure -> newSection(NewSection.Params(pieceId, parentId)).first()
        }
    }

    private fun changeSection(foo: Section.() -> Section) {
        currentSection.value = foo(currentSection.value)
    }

    fun save() = CoroutineScope(Job()).launch {
        saveSection(SaveSection.Params(currentSection.value)).collect()
    }

    fun submitAction(action: SectionActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}