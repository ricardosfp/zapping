package com.ricardosfp.zapping.data.repository.model.result

import com.ricardosfp.zapping.data.repository.model.*
import java.util.*

sealed class RssParseResult

data class RssParseSuccess(private val itemsParameter: List<MyRssItem>): RssParseResult() {
    val items: List<MyRssItem> = Collections.unmodifiableList(itemsParameter)
}

sealed class RssParseError: RssParseResult()

data class RssParseException(val exception: Exception): RssParseError()