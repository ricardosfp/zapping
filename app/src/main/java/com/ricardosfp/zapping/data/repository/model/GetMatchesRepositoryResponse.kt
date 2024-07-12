package com.ricardosfp.zapping.data.repository.model

sealed class GetMatchesRepositoryResponse

class GetMatchesRepositoryResponseSuccess(val articles: List<MyArticle>):
    GetMatchesRepositoryResponse()

class GetMatchesRepositoryResponseError(val throwable: Throwable): GetMatchesRepositoryResponse()