package com.grommade.lazymusicianship.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(milli: Long): LocalDate = LocalDate.ofEpochDay(milli)

    @TypeConverter
    fun fromListLong(value: List<Long>): String = value.joinToString(",")

    @TypeConverter
    fun toListLong(value: String): List<Long> = when (value) {
        "" -> emptyList()
        else -> value.split(",").map { it.toLong() }
    }
}