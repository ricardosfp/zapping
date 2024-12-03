package com.ricardosfp.zapping.data.repository.contract

import com.ricardosfp.zapping.data.repository.model.result.*

interface MyHttpClient {
    suspend fun getAsString(url: String): HttpGetResult
}