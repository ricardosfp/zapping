package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.domain.match.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindMatchParser(matchParser: MatchParserImpl): MatchParser

}