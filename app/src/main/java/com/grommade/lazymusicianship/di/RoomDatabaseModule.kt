package com.grommade.lazymusicianship.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
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
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "lazy_musicianship_2021"

        )
            .fallbackToDestructiveMigration()
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