package com.grommade.lazymusicianship.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate) = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(milli: Long) = LocalDate.ofEpochDay(milli)
}