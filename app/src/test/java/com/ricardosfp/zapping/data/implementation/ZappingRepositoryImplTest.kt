package com.ricardosfp.zapping.data.implementation

import com.ricardosfp.zapping.data.repository.contract.MyHttpClient
import com.ricardosfp.zapping.data.repository.contract.MyRssParser
import com.ricardosfp.zapping.data.repository.implementation.ZappingRepositoryImpl
import com.ricardosfp.zapping.data.repository.model.MyArticle
import com.ricardosfp.zapping.data.repository.model.MyRssItem
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesHttpError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesOtherExceptionError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesParseError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesSuccess
import com.ricardosfp.zapping.data.repository.model.result.HttpGetException
import com.ricardosfp.zapping.data.repository.model.result.HttpGetSuccess
import com.ricardosfp.zapping.data.repository.model.result.HttpGetUnsuccessfulResponse
import com.ricardosfp.zapping.data.repository.model.result.RssParseException
import com.ricardosfp.zapping.data.repository.model.result.RssParseSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.internal.immutableListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
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

        private val expectedRepositoryOutput = immutableListOf(
            MyArticle(
                title = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1",
                date = "Fri, 08 Nov 2024 14:45:00"),
            MyArticle(
                title = "Al-Riyadh x Al Nassr - " + "08/11 17:00 - SportTV 1",
                date = "Fri, 08 Nov 2024 17:00:00"),
            MyArticle(
                title = "FC Vizela x GD Chaves - 08/11 18:00 - SportTV +",
                date = "Fri, 08 Nov 2024 18:00:00"))
    }

    @BeforeEach
    fun setupInstance() {
        myHttpClient = mockk()
        myRssParser = mockk()
        zappingRepository = ZappingRepositoryImpl(myHttpClient, myRssParser)
    }

    @Test
    fun `unsuccessful http response returns GetArticlesHttpError`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetUnsuccessfulResponse("")
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesHttpError::class, result::class)
    }

    @Test
    fun `exception in http result returns GetArticlesHttpError`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetException(Exception())
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesHttpError::class, result::class)
    }

    @Test
    fun `good http result and bad parsing returns GetArticlesParseError`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
        coEvery { myRssParser.parse(any()) } returns RssParseException(Exception())

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesParseError::class, result::class)
    }

    @Test
    fun `exception thrown returns GetArticlesOtherExceptionError`() = runTest {
        val exceptionThrown = Exception()

        coEvery { myHttpClient.getAsString(any()) } throws exceptionThrown
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesOtherExceptionError::class, result::class)
        assertEquals(exceptionThrown, (result as GetArticlesOtherExceptionError).exception)
    }

    @Test
    fun `good http result and good parsing returns GetArticlesSuccess`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesSuccess::class, result::class)
        assertIterableEquals(expectedRepositoryOutput, (result as GetArticlesSuccess).articles)
    }


}