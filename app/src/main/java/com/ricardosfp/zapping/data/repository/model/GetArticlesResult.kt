package com.ricardosfp.zapping.data.repository.model

sealed class GetArticlesResult

data class GetArticlesSuccess(val articles: List<MyArticle>): GetArticlesResult()

sealed class GetArticlesError: GetArticlesResult()

data object GetArticlesHttpError: GetArticlesError()

data object GetArticlesParseError: GetArticlesError()

data class GetArticlesOtherExceptionError(val exception: Exception): GetArticlesError()