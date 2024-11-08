package com.ricardosfp.zapping.data.repository.implementation

import com.prof18.rssparser.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import javax.inject.*

// todo test the rss parser, but first it should should receive the text string and not a url
@Singleton
class MyRssParserImpl @Inject constructor(private val parser: RssParser): MyRssParser {

    override suspend fun parse(url: String): RssParseResult {
        return try {
            // todo check if the library automatically changes the context of the coroutine
            val channel = parser.getRssChannel(url)

            // is it worth it putting this into a separate class for testing purposes?
            val articleList = channel.items.mapNotNull {
                val title = it.title
                val pubDate = it.pubDate

                if (pubDate != null && title != null) {
                    MyRssItem(title = title, pubDate = pubDate)
                } else {
                    null
                }
            }

            RssParseSuccess(articleList)
        }
        catch (ex: Exception) {
            RssParseException(ex)
        }
    }
}