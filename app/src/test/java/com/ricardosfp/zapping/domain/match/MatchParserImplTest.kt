package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import org.junit.jupiter.api.*
import java.util.*

class MatchParserImplTest {

    private lateinit var matchParser: MatchParser

    //    "Peñarol x Atlético Mineiro - 14/05 23:00 - SportTv1"
    private val validArticleDateString = "Tue, 14 May 2024 23:00:00"
    private val validArticleHomeTeam = "Peñarol"
    private val validArticleAwayTeam = "Atlético Mineiro"
    private val validArticleChannel = "SportTv1"
    private var validArticleTitle: String = String.format("%s x %s - 14/05 23:00 - %s", validArticleHomeTeam, validArticleAwayTeam, validArticleChannel)
    private val validArticle = MyArticle(validArticleDateString, validArticleTitle)

    private val invalidArticleInvalidDate = MyArticle("14 May 24", validArticleTitle)
    private val invalidArticleInvalidHomeTeam = MyArticle(validArticleDateString, " x Atlético Mineiro - 14/05 23:00 - SportTv1")
    private val invalidArticleInvalidAwayTeam = MyArticle(validArticleDateString, "Peñarol x  - 14/05 23:00 - SportTv1")
    private val invalidArticleInvalidChannel = MyArticle(validArticleDateString, "Peñarol x Atlético Mineiro - 14/05 23:00 - ")

    @BeforeEach
    fun setupInstance() {
        val dateUtils = DateUtilsImpl()
        matchParser = MatchParserImpl(dateUtils)
    }

    @Test
    fun parseValidArticle_success() {
        val matchParseResult = matchParser.parse(validArticle)

        Assertions.assertEquals(matchParseResult::class, MatchParseSuccess::class)

        val match = (matchParseResult as MatchParseSuccess).match

        Assertions.assertEquals(validArticleTitle, match.originalText)
        Assertions.assertEquals(validArticleHomeTeam, match.homeTeam)
        Assertions.assertEquals(validArticleAwayTeam, match.awayTeam)
        Assertions.assertEquals(Date(2024 - 1900, 4, 14, 23, 0, 0), match.date)
        Assertions.assertEquals(validArticleChannel, match.channel)
        Assertions.assertEquals(validArticleTitle, match.originalText)
    }

    @Test
    fun parseInvalidDate_dateError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidDate)
        Assertions.assertEquals(MatchParseDateError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidHomeTeam_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidHomeTeam)
        Assertions.assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidAwayTeam_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidAwayTeam)
        Assertions.assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }

    @Test
    fun parseInvalidChannel_titleError() {
        val matchParseResult = matchParser.parse(invalidArticleInvalidChannel)
        Assertions.assertEquals(MatchParseTitleError::class, matchParseResult::class)
    }
}