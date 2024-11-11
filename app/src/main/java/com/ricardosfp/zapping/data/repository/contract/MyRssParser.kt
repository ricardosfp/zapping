package com.ricardosfp.zapping.data.repository.contract

import com.ricardosfp.zapping.data.repository.model.*

/**
 * It has this name to avoid confusion with [com.prof18.rssparser.RssParser]
 */
interface MyRssParser {
    suspend fun parse(rssString: String): RssParseResult
}