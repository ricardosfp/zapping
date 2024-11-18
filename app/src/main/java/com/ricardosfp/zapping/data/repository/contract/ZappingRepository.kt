package com.ricardosfp.zapping.data.repository.contract

import com.ricardosfp.zapping.data.repository.model.result.*

interface ZappingRepository {

    suspend fun getArticles(url: String): GetArticlesResult
}