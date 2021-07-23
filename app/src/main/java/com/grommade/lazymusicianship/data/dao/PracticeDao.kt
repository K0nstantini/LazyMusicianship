package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PracticeDao : EntityDao<Practice>() {

    @Query("SELECT * FROM practice_table WHERE practice_id = :id")
    abstract suspend fun getPractice(id: Long): Practice?

    @Query("SELECT * FROM practice_table WHERE practice_id = :id")
    abstract suspend fun getPracticeItem(id: Long): PracticeWithPieceAndSections?

    @Query("SELECT * FROM practice_table ORDER BY date DESC, practice_id DESC")
    abstract fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>

    @Query("SELECT * FROM practice_table ORDER BY date DESC")
    abstract fun getPracticesFlow(): Flow<List<Practice>>

    companion object {
        const val practiceWithPieceAndSections =
            """
            SELECT practice.*, piece.*, sectionFrom.*, sectionTo.*
            FROM practice_table as practice
            JOIN piece_table as piece
            ON practice.practice_id = piece.piece_id
            JOIN section_table as sectionFrom
            ON practice.sectionIdFrom = sectionFrom.section_id
            JOIN section_table as sectionTo
            ON practice.sectionIdTo = sectionTo.section_id
            ORDER BY practice.date DESC
            """
    }
}