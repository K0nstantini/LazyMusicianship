package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.DataTransfer
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Settings

@Dao
abstract class DataTransferDao: EntityDao<DataTransfer>() {

    @Query("DELETE FROM data_transfer_table")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM data_transfer_table WHERE id = 1")
    abstract suspend fun getData(): DataTransfer?
}