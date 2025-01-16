package com.ricardosfp.zapping.data.repository.zapping

import com.ricardosfp.zapping.data.repository.httpclient.MyHttpClient
import com.ricardosfp.zapping.data.repository.rss.MyRssParser
import com.ricardosfp.zapping.data.repository.zapping.model.MyArticle
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesHttpError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesOtherExceptionError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesParseError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesResult
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesSuccess
import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetError
import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetSuccess
import com.ricardosfp.zapping.data.repository.rss.model.RssParseError
import com.ricardosfp.zapping.data.repository.rss.model.RssParseSuccess
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
