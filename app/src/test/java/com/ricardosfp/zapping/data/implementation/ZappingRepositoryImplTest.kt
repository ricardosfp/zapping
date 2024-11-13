package com.ricardosfp.zapping.data.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.implementation.*
import com.ricardosfp.zapping.data.repository.model.*
import io.mockk.*
import kotlinx.coroutines.test.*
import okhttp3.internal.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

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
    fun `good parsing returns GetArticlesSuccess`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(parserOutput)

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesSuccess::class, result::class)
        if (result is GetArticlesSuccess) {
            assertIterableEquals(expectedRepositoryOutput, result.articles)
        }
    }

    @Test
    fun `bad parsing returns GetArticlesParseError`() = runTest {

        coEvery { myHttpClient.getAsString(any()) } returns HttpGetSuccess("")
        coEvery { myRssParser.parse(any()) } returns RssParseException(Exception())

        val result = zappingRepository.getArticles("")
        assertEquals(GetArticlesParseError::class, result::class)
    }
}