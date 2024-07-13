package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.*

interface MatchParser {

    fun parse(article: MyArticle): MatchParseResult
}