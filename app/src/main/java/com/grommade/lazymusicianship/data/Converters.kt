package com.grommade.lazymusicianship.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(milli: Long): LocalDate = LocalDate.ofEpochDay(milli)
}