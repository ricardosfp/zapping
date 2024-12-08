package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.MyArticle
import com.ricardosfp.zapping.domain.model.MatchParseResult

interface MatchParser {

    fun parse(article: MyArticle): MatchParseResult
}