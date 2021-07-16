package com.grommade.lazymusicianship.di

import com.grommade.lazymusicianship.data.dao.*
import com.grommade.lazymusicianship.data.repos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepositorySettings(settingsDao: SettingsDao) = RepoSettings(settingsDao)

    @Provides
    @Singleton
    fun provideRepositoryPiece(pieceDao: PieceDao) = RepoPiece(pieceDao)

    @Provides
    @Singleton
    fun provideRepositorySection(sectionDao: SectionDao) = RepoSection(sectionDao)

    @Provides
    @Singleton
    fun provideRepositoryPractice(practiceDao: PracticeDao) = RepoPractice(practiceDao)

    @Provides
    @Singleton
    fun provideRepositoryStateStudyPiece(stateDao: StateStudyPieceDao) = RepoStateStudyPiece(stateDao)

    @Provides
    @Singleton
    fun provideRepositoryStateStudySection(stateDao: StateStudySectionDao) = RepoStateStudySection(stateDao)
}