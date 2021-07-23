package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.StateStudy
import kotlinx.coroutines.flow.Flow

interface RepoStateStudy {
    suspend fun save(state: StateStudy): Long
    suspend fun delete(state: StateStudy)
    suspend fun getState(id: Long): StateStudy?
    fun getStatesFlow(): Flow<List<StateStudy>>
}