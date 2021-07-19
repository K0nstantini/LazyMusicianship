package com.grommade.lazymusicianship.data.dao

import androidx.room.*
import com.grommade.lazymusicianship.data.entity.AppEntity

abstract class EntityDao<in E : AppEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: E): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun delete(entity: E)

    @Transaction
    open suspend fun delete(entities: List<E>) {
        entities.forEach {
            delete(it)
        }
    }

    suspend fun insertOrUpdate(entity: E): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }
}