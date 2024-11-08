package com.ricardosfp.zapping.data.repository.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import javax.inject.*

class ZappingRepositoryImpl @Inject constructor(private val parser: MyRssParser):
    ZappingRepository {

    // todo handle parser/network errors
    override suspend fun getArticles(url: String): GetArticlesResult {

        return try {
            val rssParseResult = parser.parse(url)

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