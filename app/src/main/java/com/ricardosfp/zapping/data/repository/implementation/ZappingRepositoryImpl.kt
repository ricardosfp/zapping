package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.MyHttpClient
import com.ricardosfp.zapping.data.repository.contract.MyRssParser
import com.ricardosfp.zapping.data.repository.contract.ZappingRepository
import com.ricardosfp.zapping.data.repository.model.MyArticle
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesHttpError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesOtherExceptionError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesParseError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesResult
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesSuccess
import com.ricardosfp.zapping.data.repository.model.result.HttpGetError
import com.ricardosfp.zapping.data.repository.model.result.HttpGetSuccess
import com.ricardosfp.zapping.data.repository.model.result.RssParseError
import com.ricardosfp.zapping.data.repository.model.result.RssParseSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZappingRepositoryImpl @Inject constructor(
    private val httpClient: MyHttpClient, private val parser: MyRssParser
): ZappingRepository {

    override suspend fun getArticles(url: String): GetArticlesResult {

        return try {
            val httpGetResult = httpClient.getAsString(url)

            when (httpGetResult) {
                is HttpGetSuccess -> {
                    val rssParseResult = parser.parse(httpGetResult.bodyAsString)

                    when (rssParseResult) {
                        is RssParseSuccess -> {
                            GetArticlesSuccess(rssParseResult.items.map {
                                MyArticle(title = it.title, date = it.pubDate)
                            })
                        }

                        is RssParseError -> {
                            GetArticlesParseError
                        }
                    }
                }

                is HttpGetError -> GetArticlesHttpError
            }
        }
        catch (ex: Exception) {
            GetArticlesOtherExceptionError(ex)
        }

    }
}
