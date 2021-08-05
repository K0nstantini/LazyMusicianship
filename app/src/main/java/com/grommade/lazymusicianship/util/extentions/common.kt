package com.grommade.lazymusicianship.util.extentions

import com.grommade.lazymusicianship.util.MINUTES_IN_HOUR
import com.grommade.lazymusicianship.util.SECONDS_IN_MINUTE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime

fun String.toTime() =
    dropLast(3).toInt() * 60 + this.drop(3).toInt()

fun Int.toStrTime(): String {
    return (this / 60).toString().padStart(2, '0') + ':' +
            (this % 60).toString().padStart(2, '0')
}

fun Int.secondsToLocalTime(): LocalTime =
    LocalTime.of(0, this / SECONDS_IN_MINUTE, this % SECONDS_IN_MINUTE)

fun Int.minutesToLocalTime(): LocalTime =
    LocalTime.of(this / MINUTES_IN_HOUR, this % MINUTES_IN_HOUR)

fun LocalTime.toSeconds(): Int =
    hour * MINUTES_IN_HOUR + minute * SECONDS_IN_MINUTE + second

fun LocalTime.toMinutes(): Int =
    hour * MINUTES_IN_HOUR + minute

fun LocalDate.toString(default: String) = when {
    this.isEmpty() -> default
    else -> this.toString()
}

fun LocalDate.isEmpty() = when {
    this == LocalDate.MIN || this == LocalDate.MAX -> true
    else -> false
}

fun LocalDate.isNoEmpty() = !this.isEmpty()

fun <T> delayFlow(timeout: Long, value: T): Flow<T> = flow {
    delay(timeout)
    emit(value)
}