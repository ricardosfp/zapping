package com.ricardosfp.zapping.data.repository.model

sealed class GetMatchesRepositoryResponse

data class GetMatchesRepositoryResponseSuccess(val articles: List<MyArticle>):
    GetMatchesRepositoryResponse()

data class GetMatchesRepositoryResponseError(val throwable: Throwable): GetMatchesRepositoryResponse()