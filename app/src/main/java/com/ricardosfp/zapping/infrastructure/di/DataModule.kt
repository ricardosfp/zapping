package com.ricardosfp.zapping.infrastructure.di

import com.prof18.rssparser.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.implementation.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import okhttp3.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMyHttpClient(myHttpClient: MyHttpClientImpl): MyHttpClient

    @Binds
    @Singleton
    abstract fun bindMyRssParser(myRssParser: MyRssParserImpl): MyRssParser

    @Binds
    @Singleton
    abstract fun bindZappingRepository(zappingRepository: ZappingRepositoryImpl): ZappingRepository

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient()
        }

        @Provides
        @Singleton
        fun provideRssParser(): RssParser {
            return RssParserBuilder(charset = Charsets.ISO_8859_1).build()
        }
    }
}