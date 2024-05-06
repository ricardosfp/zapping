package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.implementation.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindZappingRepository(zappingRepository: ZappingRepositoryImpl): ZappingRepository

}