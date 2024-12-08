package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.MyHttpClient
import com.ricardosfp.zapping.data.repository.model.result.HttpGetException
import com.ricardosfp.zapping.data.repository.model.result.HttpGetSuccess
import com.ricardosfp.zapping.data.repository.model.result.HttpGetUnsuccessfulResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

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