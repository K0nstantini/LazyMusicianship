package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PracticeDao : EntityDao<Practice>() {

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

    @Query("SELECT * FROM practice_table ORDER BY date DESC, practice_id DESC")
    abstract fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>

}