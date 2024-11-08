package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*

class MatchParserImplTest {

    private lateinit var matchParser: MatchParser

    companion object {
        //    "Peñarol x Atlético Mineiro - 14/05 23:00 - SportTv1"
        private const val VALID_ARTICLE_DATE_STRING = "Tue, 14 May 2024 23:00:00"
        private const val VALID_ARTICLE_HOME_TEAM = "Peñarol"
        private const val VALID_ARTICLE_AWAY_TEAM = "Atlético Mineiro"
        private const val VALID_ARTICLE_CHANNEL = "SportTv1"
        private val validArticleTitle = String.format(
            "%s x %s - 14/05 23:00 - %s",
            VALID_ARTICLE_HOME_TEAM,
            VALID_ARTICLE_AWAY_TEAM,
            VALID_ARTICLE_CHANNEL)
        private val validArticle = MyArticle(
            title = validArticleTitle, date = VALID_ARTICLE_DATE_STRING)

        // invalid articles
        private val invalidArticleInvalidDate = MyArticle(
            title = validArticleTitle, date = "14 May 24")
        private val invalidArticleInvalidHomeTeam = MyArticle(
            title = " x " + "Atlético Mineiro - 14/05 23:00 - SportTv1",
            date = VALID_ARTICLE_DATE_STRING)
        private val invalidArticleInvalidAwayTeam = MyArticle(
            title = "Peñarol x  - 14/05 23:00 - SportTv1", date = VALID_ARTICLE_DATE_STRING)
        private val invalidArticleInvalidChannel = MyArticle(
            title = "Peñarol x " + "Atlético Mineiro - 14/05 23:00 - ",
            date = VALID_ARTICLE_DATE_STRING)
    }


    @BeforeEach
    fun setupInstance() {
        val dateUtils = DateUtilsImpl()
        matchParser = MatchParserImpl(dateUtils)
    }

    @Test
    fun parseValidArticle_success() {
        val matchParseResult = matchParser.parse(validArticle)

        assertEquals(matchParseResult::class, MatchParseSuccess::class)

        val match = (matchParseResult as MatchParseSuccess).match

        assertEquals(validArticleTitle, match.originalText)
        assertEquals(VALID_ARTICLE_HOME_TEAM, match.homeTeam)
        assertEquals(VALID_ARTICLE_AWAY_TEAM, match.awayTeam)
        assertEquals(Date(2024 - 1900, 4, 14, 23, 0, 0), match.date)
        assertEquals(VALID_ARTICLE_CHANNEL, match.channel)
        assertEquals(validArticleTitle, match.originalText)
    }

    @Test
    fun parseInvalidDate_dateError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidDate)
        assertEquals(MatchParseDateError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidHomeTeam_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidHomeTeam)
        assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidAwayTeam_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidAwayTeam)
        assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidChannel_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidChannel)
        assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }
}