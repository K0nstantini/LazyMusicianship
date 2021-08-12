package com.grommade.lazymusicianship.util.extentions

import com.grommade.lazymusicianship.util.MINUTES_IN_HOUR
import com.grommade.lazymusicianship.util.MONTHS_IN_YEAR
import com.grommade.lazymusicianship.util.SECONDS_IN_MINUTE
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

fun LocalDate.sameDay(date: LocalDate) =
    date.year == year && date.month == month && date.dayOfMonth == dayOfMonth

fun LocalDate.sameMonth(date: LocalDate) = date.year == year && date.month == month

fun LocalDate.sameYear(date: LocalDate) = date.year == year

fun LocalDate.isToday() = sameDay(LocalDate.now())

fun LocalDate.thisMonth() = sameMonth(LocalDate.now())

fun LocalDate.isEmpty() = when {
    this == LocalDate.MIN || this == LocalDate.MAX -> true
    else -> false
}

fun LocalDate.isNoEmpty() = !this.isEmpty()

fun LocalDate.stringYear() = "'" + year.toString().takeLast(2)

fun LocalDate.stringMonth(short: Boolean = false): String {
    val value = month.name.lowercase().replaceFirstChar { it.uppercase() }
    return if (short) value.take(3) else value
}

fun LocalDate.stringDay() =
    dayOfMonth.toString().padStart(2, '0')

fun LocalDate.diffMonths(date: LocalDate) =
    (monthValue - date.monthValue) + MONTHS_IN_YEAR * (year - date.year)