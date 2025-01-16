package com.ricardosfp.zapping.data.repository.zapping

import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesResult

interface ZappingRepository {
    suspend fun getArticles(url: String): GetArticlesResult
}