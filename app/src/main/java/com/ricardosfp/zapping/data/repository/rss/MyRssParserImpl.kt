package com.ricardosfp.zapping.data.repository.rss

import com.prof18.rssparser.RssParser
import com.ricardosfp.zapping.data.repository.rss.model.MyRssItem
import com.ricardosfp.zapping.data.repository.rss.model.RssParseException
import com.ricardosfp.zapping.data.repository.rss.model.RssParseResult
import com.ricardosfp.zapping.data.repository.rss.model.RssParseSuccess
import com.ricardosfp.zapping.infrastructure.di.DispatcherModule.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyRssParserImpl @Inject constructor(
    private val parser: RssParser,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): MyRssParser {

    // todo test
    override suspend fun parse(rssString: String): RssParseResult = withContext(defaultDispatcher) {
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