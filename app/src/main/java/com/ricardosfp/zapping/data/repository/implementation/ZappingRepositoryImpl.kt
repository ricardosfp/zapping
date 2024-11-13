package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import javax.inject.*

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
