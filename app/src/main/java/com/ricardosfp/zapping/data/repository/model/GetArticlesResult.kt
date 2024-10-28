package com.ricardosfp.zapping.data.repository.model

sealed class GetArticlesResult

data class GetArticlesSuccess(val articles: List<MyArticle>):
    GetArticlesResult()

data class GetArticlesError(val throwable: Throwable): GetArticlesResult()