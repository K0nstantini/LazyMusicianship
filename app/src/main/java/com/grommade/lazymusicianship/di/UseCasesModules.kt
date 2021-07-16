package com.grommade.lazymusicianship.di

import com.grommade.lazymusicianship.use_cases.PopulateDBWithPieces
import com.grommade.lazymusicianship.use_cases.PopulateDBWithPiecesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCasesModules {

    @Binds
    @Singleton
    abstract fun bindPopulateDBWithPieces(populateDBWithPiecesImpl: PopulateDBWithPiecesImpl): PopulateDBWithPieces
}