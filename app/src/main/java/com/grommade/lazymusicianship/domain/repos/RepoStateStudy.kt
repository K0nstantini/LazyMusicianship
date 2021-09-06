package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.flow.Flow

interface RepoStateStudy {
    suspend fun save(state: StateStudy): Long
    suspend fun delete(state: StateStudy): ResultOf<Boolean>
    suspend fun deleteAll()
    suspend fun getState(id: Long): StateStudy?
    suspend fun getAllStates(): List<StateStudy>
    fun getStatesFlow(): Flow<List<StateStudy>>
}