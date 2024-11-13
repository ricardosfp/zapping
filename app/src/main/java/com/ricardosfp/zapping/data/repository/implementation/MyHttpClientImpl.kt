package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import kotlinx.coroutines.*
import okhttp3.*
import javax.inject.*

@Singleton
class MyHttpClientImpl @Inject constructor(private val client: OkHttpClient): MyHttpClient {

    override suspend fun getAsString(url: String) = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                    .url(url)
                    .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    HttpGetSuccess(
                        response.body!!.source().readString(Charsets.ISO_8859_1))
                } else {
                    HttpGetUnsuccessfulResponse(response.body!!.string())
                }
            }
        }
        catch (ex: Exception) {
            HttpGetException(ex)
        }
    }
}