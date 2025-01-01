package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.MyHttpClient
import com.ricardosfp.zapping.data.repository.model.result.HttpGetException
import com.ricardosfp.zapping.data.repository.model.result.HttpGetNoBody
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
            val request = Request.Builder().url(url).build()

            // the [okhttp3.Response] automatically closes the [okhttp3.ResponseBody]
            client.newCall(request).execute().use { response ->
                val body = response.body
                if (body == null) {
                    HttpGetNoBody
                } else {
                    if (response.isSuccessful) {
                        HttpGetSuccess(
                            body.source().readString(Charsets.ISO_8859_1))
                    } else {
                        HttpGetUnsuccessfulResponse(body.string())
                    }
                }
            }
        }
        catch (ex: Exception) {
            HttpGetException(ex)
        }
    }
}