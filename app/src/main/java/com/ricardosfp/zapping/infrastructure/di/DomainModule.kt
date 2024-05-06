package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.domain.article.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindArticleParser(articleParser: ArticleParserImpl): ArticleParser

}