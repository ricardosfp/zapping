package com.ricardosfp.zapping.infrastructure.di

import com.prof18.rssparser.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.implementation.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindZappingRepository(zappingRepository: ZappingRepositoryImpl): ZappingRepository

    @Binds
    @Singleton
    abstract fun bindMyRssParser(myRssParser: MyRssParserImpl): MyRssParser

    companion object {
        @Provides
        @Singleton
        fun provideRssParser(): RssParser {
            return RssParser()
        }
    }
}