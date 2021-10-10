package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
abstract class PracticeDao : EntityDao<Practice>() {

    @Query("DELETE FROM practice_table")
    abstract suspend fun deleteAll()

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
    abstract fun practicesWithDetailsFlow(): Flow<List<PracticeWithDetails>>

    @Transaction
    @Query("SELECT * FROM practice_table WHERE practice_id = :id")
    abstract fun practiceWithDetails(id: Long): PracticeWithDetails?


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