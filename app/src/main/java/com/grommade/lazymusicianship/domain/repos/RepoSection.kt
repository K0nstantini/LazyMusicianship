package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.flow.Flow

interface RepoSection {
    suspend fun save(section: Section): Long
    suspend fun delete(section: Section): ResultOf<Boolean>
    suspend fun deleteAll()
    suspend fun getSection(id: Long): Section?
    suspend fun getLastCreated(pieceId: Long): Section?
    fun getSectionsFlow(pieceId: Long): Flow<List<Section>>
}