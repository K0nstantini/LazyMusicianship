package com.grommade.lazymusicianship.di

import com.grommade.lazymusicianship.domain.use_cases.PopulateDBWithPieces
import com.grommade.lazymusicianship.domain.use_cases.PopulateDBWithPiecesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModules {

    @Binds
    @Singleton
    abstract fun bindPopulateDBWithPieces(populateDBWithPiecesImpl: PopulateDBWithPiecesImpl): PopulateDBWithPieces
}