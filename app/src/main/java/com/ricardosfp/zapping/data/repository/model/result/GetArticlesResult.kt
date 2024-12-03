package com.ricardosfp.zapping.data.repository.model.result

import com.ricardosfp.zapping.data.repository.model.*
import java.util.*

sealed class GetArticlesResult

data class GetArticlesSuccess(private val articlesParameter: List<MyArticle>): GetArticlesResult() {
    val articles: List<MyArticle> = Collections.unmodifiableList(articlesParameter)
}

sealed class GetArticlesError: GetArticlesResult()

data object GetArticlesHttpError: GetArticlesError()

data object GetArticlesParseError: GetArticlesError()

data class GetArticlesOtherExceptionError(val exception: Exception): GetArticlesError()