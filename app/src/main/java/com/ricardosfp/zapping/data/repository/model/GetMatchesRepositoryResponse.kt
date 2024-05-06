package com.ricardosfp.zapping.data.repository.model

import com.prof.rssparser.*

sealed class GetMatchesRepositoryResponse

class GetMatchesRepositoryResponseSuccess(val channel: Channel): GetMatchesRepositoryResponse()

class GetMatchesRepositoryResponseError(val throwable: Throwable): GetMatchesRepositoryResponse()