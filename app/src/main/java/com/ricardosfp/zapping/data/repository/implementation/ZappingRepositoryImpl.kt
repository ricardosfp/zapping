package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import javax.inject.*

class ZappingRepositoryImpl @Inject constructor(
    private val parser: MyRssParser, private val httpClient: MyHttpClient
): ZappingRepository {

    // todo handle parser/network errors
    override suspend fun getArticles(url: String): GetArticlesResult {

        return try {
            val rssString = httpClient.getAsString(url)

            val rssParseResult = parser.parse(rssString)

            when (rssParseResult) {
                is RssParseSuccess -> {
                    GetArticlesSuccess(rssParseResult.items.map {
                        MyArticle(title = it.title, date = it.pubDate)
                    })
                }

                is RssParseException -> {
                    GetArticlesError(rssParseResult.exception)
                }
            }
        }
        catch (ex: Exception) {
            GetArticlesError(ex)
        }

    }
}
