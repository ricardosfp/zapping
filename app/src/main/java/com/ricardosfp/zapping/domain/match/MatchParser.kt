package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.zapping.model.MyArticle
import com.ricardosfp.zapping.domain.match.model.MatchParseResult

interface MatchParser {

    fun parse(article: MyArticle): MatchParseResult
}