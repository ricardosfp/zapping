package com.ricardosfp.zapping.data.repository.model

// this could be generic
sealed class HttpGetResult

class HttpGetSuccess(val bodyAsString: String): HttpGetResult()

sealed class HttpGetError: HttpGetResult()

// maybe add the http response code
class HttpGetUnsuccessfulResponse(val bodyAsString: String): HttpGetError()

class HttpGetException(val exception: Exception): HttpGetError()