package com.ricardosfp.zapping.data.repository.implementation

import com.prof18.rssparser.RssParser
import com.ricardosfp.zapping.data.repository.contract.MyRssParser
import com.ricardosfp.zapping.data.repository.model.MyRssItem
import com.ricardosfp.zapping.data.repository.model.result.RssParseException
import com.ricardosfp.zapping.data.repository.model.result.RssParseResult
import com.ricardosfp.zapping.data.repository.model.result.RssParseSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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