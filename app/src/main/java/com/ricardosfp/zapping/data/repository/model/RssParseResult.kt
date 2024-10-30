package com.ricardosfp.zapping.data.repository.model

sealed class RssParseResult

class RssParseSuccess(val items: List<MyRssItem>): RssParseResult()

sealed class RssParseError: RssParseResult()

class RssParseException(val exception: Exception): RssParseError()