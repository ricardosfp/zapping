package com.ricardosfp.zapping.data.repository.contract

import com.ricardosfp.zapping.data.repository.model.result.HttpGetResult

interface MyHttpClient {
    suspend fun getAsString(url: String): HttpGetResult
}