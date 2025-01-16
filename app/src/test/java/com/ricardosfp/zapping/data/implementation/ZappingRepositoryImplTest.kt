package com.ricardosfp.zapping.data.implementation

import com.ricardosfp.zapping.data.repository.httpclient.MyHttpClient
import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetException
import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetSuccess
import com.ricardosfp.zapping.data.repository.httpclient.model.HttpGetUnsuccessfulResponse
import com.ricardosfp.zapping.data.repository.rss.MyRssParser
import com.ricardosfp.zapping.data.repository.rss.model.MyRssItem
import com.ricardosfp.zapping.data.repository.rss.model.RssParseException
import com.ricardosfp.zapping.data.repository.rss.model.RssParseSuccess
import com.ricardosfp.zapping.data.repository.zapping.ZappingRepositoryImpl
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesHttpError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesOtherExceptionError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesParseError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesSuccess
import com.ricardosfp.zapping.data.repository.zapping.model.MyArticle
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.internal.immutableListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ZappingRepositoryImplTest {

    private lateinit var myHttpClient: MyHttpClient
    private lateinit var myRssParser: MyRssParser
    private lateinit var zappingRepository: ZappingRepositoryImpl

    companion object {
        private val parserOutput = immutableListOf(
            MyRssItem(
                title = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1",
                pubDate = "Fri, 08 Nov 2024 14:45:00"),
            MyRssItem(
                title = "Al-Riyadh x Al Nassr - " + "08/11 17:00 - SportTV 1",
                pubDate = "Fri, 08 Nov 2024 17:00:00"),
            MyRssItem(
                title = "FC Vizela x GD Chaves - 08/11 18:00 - SportTV +",
                pubDate = "Fri, 08 Nov 2024 18:00:00"))

        private val expectedRepositoryOutput = GetArticlesSuccess(
            immutableListOf(
                MyArticle(
                    title = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1",
                    date = "Fri, 08 Nov 2024 14:45:00"),
                MyArticle(
                    title = "Al-Riyadh x Al Nassr - " + "08/11 17:00 - SportTV 1",
                    date = "Fri, 08 Nov 2024 17:00:00"),
                MyArticle(
                    title = "FC Vizela x GD Chaves - 08/11 18:00 - SportTV +",
                    date = "Fri, 08 Nov 2024 18:00:00")))
    }

    @BeforeEach
    fun setupInstance() {
        myHttpClient = mockk()
        myRssParser = mockk()
        zappingRepository = ZappingRepositoryImpl(myHttpClient, myRssParser)
    }

    @Test
    @DisplayName("good http response and good parsing returns GetArticlesSuccess")
    fun goodHttpResponseAndGoodParsing_success() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")

        assertEquals(expectedRepositoryOutput, result)
    }

    @Nested
    inner class FailureTests {
        @Test
        @DisplayName("unsuccessful http response returns GetArticlesHttpError")
        fun unsuccessfulHttpResponse_httpError() = runTest {

            coEvery { myHttpClient.getAsString(any()) } returns HttpGetUnsuccessfulResponse("")
            coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

            val result = zappingRepository.getArticles("")

            assertEquals(GetArticlesHttpError::class, result::class)
        }

        @Test
        @DisplayName("exception in http response returns GetArticlesHttpError")
        fun exceptionHttpResponse_httpError() = runTest {

            coEvery { myHttpClient.getAsString(any()) } returns HttpGetException(Exception())
            coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

            val result = zappingRepository.getArticles("")

            assertEquals(GetArticlesHttpError::class, result::class)
        }

        @Test
        @DisplayName("good http response and bad parsing returns GetArticlesParseError")
        fun goodHttpResponseAndBadParsing_parseError() = runTest {

            coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
            coEvery { myRssParser.parse(any()) } returns RssParseException(Exception())

            val result = zappingRepository.getArticles("")

            assertEquals(GetArticlesParseError::class, result::class)
        }

        @Test
        @DisplayName("exception thrown returns GetArticlesOtherExceptionError")
        fun exception_otherExceptionError() = runTest {
            val exceptionThrown = Exception()

            coEvery { myHttpClient.getAsString(any()) } throws exceptionThrown
            coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

            val result = zappingRepository.getArticles("")

            assertEquals(GetArticlesOtherExceptionError::class, result::class)
            assertEquals(exceptionThrown, (result as GetArticlesOtherExceptionError).exception)
        }
    }


}