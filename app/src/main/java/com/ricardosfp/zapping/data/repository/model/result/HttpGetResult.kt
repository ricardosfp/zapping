package com.ricardosfp.zapping.data.repository.model.result

// this could be generic
sealed class HttpGetResult

data class HttpGetSuccess(val bodyAsString: String): HttpGetResult()

sealed class HttpGetError: HttpGetResult()

// maybe add the http response code
data class HttpGetUnsuccessfulResponse(val bodyAsString: String): HttpGetError()

data class HttpGetException(val exception: Exception): HttpGetError()