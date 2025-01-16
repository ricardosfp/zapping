package com.ricardosfp.zapping.data.repository.rss

import com.ricardosfp.zapping.data.repository.rss.model.RssParseResult

/**
 * It has this name to avoid confusion with [com.prof18.rssparser.RssParser]
 */
interface MyRssParser {
    suspend fun parse(rssString: String): RssParseResult
}