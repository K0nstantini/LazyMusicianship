package com.grommade.lazymusicianship.util.extentions

import com.grommade.lazymusicianship.util.MILLI_IN_SECOND
import com.grommade.lazymusicianship.util.MINUTES_IN_HOUR
import com.grommade.lazymusicianship.util.MONTHS_IN_YEAR
import com.grommade.lazymusicianship.util.SECONDS_IN_MINUTE
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.roundToInt

fun String.toTime() =
    dropLast(3).toInt() * 60 + this.drop(3).toInt()

fun Int.minutesToStrTime(): String {
    return (this / MINUTES_IN_HOUR).toString().padStart(2, '0') + ':' +
            (this % MINUTES_IN_HOUR).toString().padStart(2, '0')
}

fun Int.secondsToStrTime(): String {
    return (this / (MINUTES_IN_HOUR * SECONDS_IN_MINUTE)).toString().padStart(2, '0') + ':' +
            (this / SECONDS_IN_MINUTE).toString().padStart(2, '0') + ':' +
            (this % SECONDS_IN_MINUTE).toString().padStart(2, '0')
}

fun Long.milliToSeconds() = (this / MILLI_IN_SECOND).toInt()

fun Long.milliToMinutes(round: Boolean = false) = when (round) {
    true -> ((this / MILLI_IN_SECOND).toFloat() / SECONDS_IN_MINUTE).roundToInt()
    false -> (this / MILLI_IN_SECOND / SECONDS_IN_MINUTE).toInt()
}

fun Int.minutesToMilli() = this.toLong() * SECONDS_IN_MINUTE * MILLI_IN_SECOND

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

fun LocalDate.sameWeek(date: LocalDate) = when {
    this > date -> dayOfWeek.ordinal > date.dayOfWeek.ordinal
    this < date -> dayOfWeek.ordinal < date.dayOfWeek.ordinal
    else -> true
} && abs(diffDays(date)) < 7

fun LocalDate.sameMonth(date: LocalDate) = date.year == year && date.month == month

fun LocalDate.sameYear(date: LocalDate) = date.year == year

fun LocalDate.isToday() = sameDay(LocalDate.now())

fun LocalDate.thisMonth() = sameMonth(LocalDate.now())

fun LocalDate.isEmpty() = when {
    this == LocalDate.MIN || this == LocalDate.MAX || this.toEpochDay() == 0L -> true
    else -> false
}

fun LocalDate.isNoEmpty() = !this.isEmpty()

fun LocalDate.stringYear() = "'" + year.toString().takeLast(2)

fun LocalDate.stringMonth(short: Boolean = false): String {
    val value = month.name.lowercase().replaceFirstChar { it.uppercase() }
    return if (short) value.take(3) else value
}

fun LocalDate.toStringFormat(): String =
    format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

fun YearMonth.asString(): String {
    val date = atEndOfMonth()
    return "${date.stringMonth()} ${date.year}"
}

fun LocalDate.stringDay() =
    dayOfMonth.toString().padStart(2, '0')

fun LocalDate.diffMonths(date: LocalDate) =
    (monthValue - date.monthValue) + MONTHS_IN_YEAR * (year - date.year)

fun LocalDate.diffDays(date: LocalDate) =
    ChronoUnit.DAYS.between(this, date).toInt()