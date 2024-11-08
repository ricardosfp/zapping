package com.ricardosfp.zapping.data.implementation

import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.implementation.*
import com.ricardosfp.zapping.data.repository.model.*
import io.mockk.*
import kotlinx.coroutines.test.*
import okhttp3.internal.*
import org.junit.jupiter.api.*

class ZappingRepositoryImplTest {

    private lateinit var zappingRepository: ZappingRepository
    private lateinit var myRssParser: MyRssParser

    @BeforeEach
    fun setupInstance() {
        myRssParser = mockk()
        zappingRepository = ZappingRepositoryImpl(myRssParser)
    }


    // todo maybe check if the conversion to [com.ricardosfp.zapping.data.repository.model
    //  .MyArticle] is correct
    @Test
    fun `good parsing returns GetArticlesSuccess`() = runTest {

        coEvery { myRssParser.parse(any()) } returns RssParseSuccess(immutableListOf())

        val result = zappingRepository.getArticles("")
        Assertions.assertEquals(GetArticlesSuccess::class, result::class)
    }
}