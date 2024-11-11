package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import kotlinx.coroutines.*
import okhttp3.*
import javax.inject.*

@Singleton
class MyHttpClientImpl @Inject constructor(private val client: OkHttpClient): MyHttpClient {

    // todo catch exceptions and return a sealed class
    override suspend fun getAsString(url: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                return@use response.body!!.source().readString(Charsets.ISO_8859_1)
            } else {
                ""
            }
        }

    }
}