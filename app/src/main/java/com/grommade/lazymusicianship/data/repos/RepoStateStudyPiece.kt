package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.StateStudyPieceDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoStateStudyPiece @Inject constructor(
    private val stateStudyPieceDao: StateStudyPieceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}