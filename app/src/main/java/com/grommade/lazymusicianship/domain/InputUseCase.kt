package com.grommade.lazymusicianship.domain

import com.grommade.lazymusicianship.util.InvokeError
import com.grommade.lazymusicianship.util.InvokeStarted
import com.grommade.lazymusicianship.util.InvokeStatus
import com.grommade.lazymusicianship.util.InvokeSuccess
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

abstract class InputWorkUseCase<in P> {
    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        return flow {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        }.catch { t ->
            emit(InvokeError(t))
        }
    }

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

abstract class ResultUserCase<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    protected abstract suspend fun doWork(params: P): R
}

abstract class ObserveUserCase<P : Any, T> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>

    fun observe(): Flow<T> = paramState.flatMapLatest { createObservable(it) }
}