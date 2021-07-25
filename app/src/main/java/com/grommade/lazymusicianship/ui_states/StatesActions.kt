package com.grommade.lazymusicianship.ui_states

import com.grommade.lazymusicianship.data.entity.StateStudy

sealed class StatesActions {
    data class Select(val id: Long) : StatesActions()
    data class Open(val id: Long) : StatesActions()
    data class Delete(val state: StateStudy) : StatesActions()
    object ClearError : StatesActions()
    object AddNew : StatesActions()
    object Back : StatesActions()
}