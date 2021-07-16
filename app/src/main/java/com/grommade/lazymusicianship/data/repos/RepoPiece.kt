package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.PieceDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoPiece @Inject constructor(
    private val pieceDao: PieceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}