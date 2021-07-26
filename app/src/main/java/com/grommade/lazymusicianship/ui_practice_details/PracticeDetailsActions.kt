package com.grommade.lazymusicianship.ui_practice_details

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.entity.StateStudy
import java.time.LocalDate

sealed class PracticeDetailsActions {
    data class ChangeDate(val date: LocalDate) : PracticeDetailsActions()
    data class ChangePiece(val piece: Piece) : PracticeDetailsActions()
    data class ChangeSectionFrom(val section: Section) : PracticeDetailsActions()
    data class ChangeSectionTo(val section: Section) : PracticeDetailsActions()
    data class ChangeTime(val value: Int) : PracticeDetailsActions()
    data class ChangeState(val state: StateStudy) : PracticeDetailsActions()
    data class ChangeTempo(val value: Int) : PracticeDetailsActions()
    data class ChangeCountTimes(val value: Int) : PracticeDetailsActions()
    object SaveAndClose : PracticeDetailsActions()
    object Close : PracticeDetailsActions()
}