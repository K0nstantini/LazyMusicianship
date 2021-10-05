package com.grommade.lazymusicianship.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grommade.lazymusicianship.data.dao.*
import com.grommade.lazymusicianship.data.entity.*

@Database(
    entities = [
        Piece::class,
        Section::class,
        Practice::class,
        StateStudy::class,
        Settings::class,
    ],
    version = 5,
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun SettingsDao(): SettingsDao
    abstract fun PieceDao(): PieceDao
    abstract fun SectionDao(): SectionDao
    abstract fun PracticeDao(): PracticeDao
    abstract fun StateStudyDao(): StateStudyDao
}