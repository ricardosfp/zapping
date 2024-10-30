package com.ricardosfp.zapping.data.repository.contract

import com.ricardosfp.zapping.data.repository.model.*

interface ZappingRepository {

    suspend fun getArticles(url: String): GetArticlesResult
}