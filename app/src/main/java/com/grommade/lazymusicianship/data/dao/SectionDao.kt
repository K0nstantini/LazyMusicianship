package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section

@Dao
abstract class SectionDao : EntityDao<Section>() {

    @Query("SELECT * FROM section_table WHERE section_id = :id")
    abstract suspend fun getSection(id: Long): Section?
}