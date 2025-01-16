package com.ricardosfp.zapping.data.repository.httpclient

import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetResult

interface MyHttpClient {
    suspend fun getAsString(url: String): HttpGetResult
}