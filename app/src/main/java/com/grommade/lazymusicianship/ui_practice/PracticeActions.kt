package com.grommade.lazymusicianship.ui_practice

import com.grommade.lazymusicianship.data.entity.Practice

sealed class PracticeActions {
    data class Select(val id: Long) : PracticeActions()
    data class Open(val id: Long) : PracticeActions()
    data class Delete(val practice: Practice) : PracticeActions()
    object AddNew : PracticeActions()
}