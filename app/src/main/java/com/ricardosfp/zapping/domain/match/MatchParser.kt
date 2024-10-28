package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.domain.model.*

interface MatchParser {

    fun parse(article: MyArticle): MatchParseResult
}