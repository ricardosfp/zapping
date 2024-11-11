package com.ricardosfp.zapping.data.repository.contract

interface MyHttpClient {
    suspend fun getAsString(url: String): String
}