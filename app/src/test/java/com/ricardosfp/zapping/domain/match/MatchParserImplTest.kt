package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.zapping.model.MyArticle
import com.ricardosfp.zapping.domain.match.model.Match
import com.ricardosfp.zapping.domain.match.model.MatchParseDateError
import com.ricardosfp.zapping.domain.match.model.MatchParseSuccess
import com.ricardosfp.zapping.domain.match.model.MatchParseTitleError
import com.ricardosfp.zapping.infrastructure.date.DateUtilsImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MatchParserImplTest {

    private lateinit var matchParser: MatchParserImpl

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

        private val expectedParserOutput = MatchParseSuccess(
            Match(
                homeTeam = VALID_ARTICLE_HOME_TEAM,
                awayTeam = VALID_ARTICLE_AWAY_TEAM,
                date = LocalDateTime.of(2024, 5, 14, 23, 0),
                channel = VALID_ARTICLE_CHANNEL,
                originalText = validArticleTitle))

        // invalid articles
        private val invalidArticleInvalidDate = MyArticle(
            title = validArticleTitle, date = "14 May 24")
        private val invalidArticleInvalidHomeTeam = MyArticle(
            title = " x Atlético Mineiro - 14/05 23:00 - SportTv1",
            date = VALID_ARTICLE_DATE_STRING)
        private val invalidArticleInvalidAwayTeam = MyArticle(
            title = "Peñarol x  - 14/05 23:00 - SportTv1", date = VALID_ARTICLE_DATE_STRING)
        private val invalidArticleInvalidChannel = MyArticle(
            title = "Peñarol x Atlético Mineiro - 14/05 23:00 - ",
            date = VALID_ARTICLE_DATE_STRING)
    }

    @BeforeEach
    fun setupInstance() {
        val dateUtils = DateUtilsImpl()
        // todo this should be mocked, or else it is an integration test
        matchParser = MatchParserImpl(dateUtils)
    }

    @Test
    @DisplayName("parse valid article returns success")
    fun parseValidArticle_success() {
        val matchParseResult = matchParser.parse(validArticle)

        assertEquals(expectedParserOutput, matchParseResult)
    }

    @Nested
    inner class FailureTests {

        @Test
        @DisplayName("parse invalid date returns date error")
        fun parseInvalidDate_dateError() {
            val matchParseResult = matchParser.parse(invalidArticleInvalidDate)

            assertEquals(MatchParseDateError::class, matchParseResult::class)
        }

        @Test
        @DisplayName("parse invalid home team returns title error")
        fun parseInvalidHomeTeam_titleError() {
            val matchParseResult = matchParser.parse(invalidArticleInvalidHomeTeam)

            assertEquals(MatchParseTitleError::class, matchParseResult::class)
        }

        @Test
        @DisplayName("parse invalid away team returns title error")
        fun parseInvalidAwayTeam_titleError() {
            val matchParseResult = matchParser.parse(invalidArticleInvalidAwayTeam)

            assertEquals(MatchParseTitleError::class, matchParseResult::class)
        }

        @Test
        @DisplayName("parse invalid channel returns title error")
        fun parseInvalidChannel_titleError() {
            val matchParseResult = matchParser.parse(invalidArticleInvalidChannel)

            assertEquals(MatchParseTitleError::class, matchParseResult::class)
        }
    }
}