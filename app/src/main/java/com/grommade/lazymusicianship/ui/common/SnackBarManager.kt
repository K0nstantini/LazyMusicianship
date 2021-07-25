/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grommade.lazymusicianship.ui.common

import com.grommade.lazymusicianship.util.extentions.delayFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.time.Duration
import javax.inject.Inject

class SnackBarManager @Inject constructor() {
    private val pendingErrors = Channel<Int>(3, BufferOverflow.DROP_OLDEST)
    private val removeErrorSignal = Channel<Unit>(Channel.RENDEZVOUS)

    val errors: Flow<Int?> = flow {
        emit(null)

        pendingErrors.receiveAsFlow().collect {
            emit(it)

            merge(
                delayFlow(Duration.ofSeconds(6).toMillis(), Unit),
                removeErrorSignal.receiveAsFlow(),
            ).firstOrNull()

            emit(null)
        }
    }

    suspend fun addError(error: Int) {
        pendingErrors.send(error)
    }

    suspend fun removeCurrentError() {
        removeErrorSignal.send(Unit)
    }
}
