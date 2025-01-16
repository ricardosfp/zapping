package com.ricardosfp.zapping.infrastructure.di

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import com.ricardosfp.zapping.data.repository.httpclient.MyHttpClient
import com.ricardosfp.zapping.data.repository.rss.MyRssParser
import com.ricardosfp.zapping.data.repository.zapping.ZappingRepository
import com.ricardosfp.zapping.data.repository.httpclient.MyHttpClientImpl
import com.ricardosfp.zapping.data.repository.rss.MyRssParserImpl
import com.ricardosfp.zapping.data.repository.zapping.ZappingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

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