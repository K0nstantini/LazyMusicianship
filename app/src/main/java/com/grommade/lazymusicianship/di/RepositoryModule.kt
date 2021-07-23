package com.grommade.lazymusicianship.di

import com.grommade.lazymusicianship.data.repos_impl.*
import com.grommade.lazymusicianship.domain.repos.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositorySettings(repo: RepoSettingsImpl): RepoSettings

    @Binds
    @Singleton
    abstract fun bindRepositoryPiece(repo: RepoPieceImpl): RepoPiece

    @Binds
    @Singleton
    abstract fun bindRepositorySection(repo: RepoSectionImpl): RepoSection

    @Binds
    @Singleton
    abstract fun bindRepositoryPractice(repo: RepoPracticeImpl): RepoPractice

    @Binds
    @Singleton
    abstract fun bindRepositoryStateStudy(repo: RepoStateStudyImpl): RepoStateStudy
}