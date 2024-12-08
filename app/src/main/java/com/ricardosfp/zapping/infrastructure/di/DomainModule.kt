package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.domain.match.MatchParser
import com.ricardosfp.zapping.domain.match.MatchParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindMatchParser(matchParser: MatchParserImpl): MatchParser

}