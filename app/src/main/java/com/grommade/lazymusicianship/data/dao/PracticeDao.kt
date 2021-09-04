package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithDetails
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
abstract class PracticeDao : EntityDao<Practice>() {

    @Transaction
    @Query("SELECT * FROM practice_table WHERE practice_id = :id")
    abstract suspend fun getPracticeItem(id: Long): PracticeWithPieceAndSections?

    @Query("SELECT COUNT(*) FROM practice_table WHERE practice_state_id = :stateId")
    abstract suspend fun getCountPracticesByStateId(stateId: Long): Int

    @Query("SELECT COUNT(*) FROM practice_table WHERE practice_piece_id = :pieceId")
    abstract suspend fun getCountPracticesByPieceId(pieceId: Long): Int

    @Query(
        "SELECT COUNT(*) FROM practice_table " +
                "WHERE practice_section_id_from IN (:sectionsId) OR practice_section_id_to IN (:sectionsId)"
    )
    abstract suspend fun getCountPracticesBySectionsId(sectionsId: List<Long>): Int

    @Transaction
    @Query("SELECT * FROM practice_table ORDER BY date DESC, practice_id DESC")
    abstract fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>

    @Transaction
    @Query("SELECT * FROM practice_table ORDER BY date DESC, practice_id DESC")
    abstract fun practiceWithDetailsFlow(): Flow<List<PracticeWithDetails>>

//    @Transaction
//    @Query("""
//        SELECT practice.*, section.*
//        FROM practice_table as practice
//        LEFT JOIN section_table as section
//        ON practice.practice_piece_id = section.section_piece_id
//        ORDER BY practice.date DESC, practice.practice_id DESC
//        """
//    )
//    abstract fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>

    @Query(
        """
        SELECT date, sum(elapsedTime) as time 
        FROM practice_table
        WHERE date >= :startDate AND date <= :endDate
        GROUP BY date 
        ORDER BY date
        """
    )
    abstract fun getTimesByDaysFlow(startDate: LocalDate, endDate: LocalDate): Flow<List<TimesByDays>>

    data class TimesByDays(val date: LocalDate, val time: Int)

}