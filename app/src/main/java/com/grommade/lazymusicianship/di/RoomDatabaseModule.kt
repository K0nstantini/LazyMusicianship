package com.grommade.lazymusicianship.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grommade.lazymusicianship.data.AppDataBase
import com.grommade.lazymusicianship.data.dao.SettingsDao
import com.grommade.lazymusicianship.data.entity.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context,
        settingsDaoProvide: Provider<SettingsDao>,
    ): AppDataBase {

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // Create the new table
                database.execSQL(
                    """
                        CREATE TABLE practice_table_new (
                            practice_id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0 NOT NULL,
                            practice_piece_id INTEGER DEFAULT 0 NOT NULL,
                            practice_section_id_from INTEGER DEFAULT 0 NOT NULL,
                            practice_section_id_to INTEGER DEFAULT 0 NOT NULL,
                            practice_state_id INTEGER DEFAULT 0 NOT NULL,
                            date INTEGER DEFAULT 0 NOT NULL,
                            elapsedTime INTEGER DEFAULT 0 NOT NULL,
                            practice_tempo INTEGER DEFAULT 0 NOT NULL,
                            countTimes INTEGER DEFAULT 0 NOT NULL
                        )
                        """
                )

                // Copy the data
                database.execSQL(
                    """
                        INSERT INTO practice_table_new
                        SELECT
                            practice_id,
                            practice_piece_id,
                            practice_section_id_from,
                            practice_section_id_to,
                            practice_state_id,
                            date,
                            elapsedTime,
                            practice_tempo,
                            countTimes
                        FROM practice_table
                        """
                )

                // Remove the old table
                database.execSQL("DROP TABLE practice_table")

                // Change the table name to the correct one
                database.execSQL("ALTER TABLE practice_table_new RENAME TO practice_table")
            }
        }

        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "lazy_musicianship_2021"

        )
            .addMigrations(MIGRATION_3_4)
//            .fallbackToDestructiveMigration()
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(SupervisorJob()).launch {
                            insertSettings()
                        }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(SupervisorJob()).launch {
                            if (settingsDaoProvide.get().getCount() == 0) {
                                insertSettings()
                            }
                        }
                    }

                    private suspend fun insertSettings() {
                        settingsDaoProvide.get().insert(Settings())
                    }

                }
            )
            .build()
    }

}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {
    @Provides
    fun provideSettingsDao(db: AppDataBase) = db.SettingsDao()

    @Provides
    fun providePieceDao(db: AppDataBase) = db.PieceDao()

    @Provides
    fun provideSectionDao(db: AppDataBase) = db.SectionDao()

    @Provides
    fun providePracticeDao(db: AppDataBase) = db.PracticeDao()

    @Provides
    fun provideStateStudyDao(db: AppDataBase) = db.StateStudyDao()

}