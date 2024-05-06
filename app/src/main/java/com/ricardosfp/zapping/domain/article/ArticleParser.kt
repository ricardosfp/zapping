package com.ricardosfp.zapping.domain.article

import com.prof.rssparser.*
import com.ricardosfp.zapping.domain.model.*

interface ArticleParser {

    fun parse(article: Article) : Match?
}