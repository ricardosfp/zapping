package com.ricardosfp.zapping.data.repository.implementation

import com.prof18.rssparser.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import kotlinx.coroutines.*
import javax.inject.*

@Singleton
class MyRssParserImpl @Inject constructor(private val parser: RssParser): MyRssParser {

    // todo test
    override suspend fun parse(rssString: String): RssParseResult = withContext(Dispatchers.IO) {
        try {
            val channel = parser.parse(rssString)

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