package com.grommade.lazymusicianship.util.extentions

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun String.toTime() =
    dropLast(3).toInt() * 60 + this.drop(3).toInt()

fun Int.toStrTime(): String {
    return (this / 60).toString().padStart(2, '0') + ':' +
            (this % 60).toString().padStart(2, '0')
}

fun <T> delayFlow(timeout: Long, value: T): Flow<T> = flow {
    delay(timeout)
    emit(value)
}