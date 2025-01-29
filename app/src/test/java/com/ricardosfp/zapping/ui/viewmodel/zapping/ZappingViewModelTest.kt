package com.ricardosfp.zapping.ui.viewmodel.zapping

import androidx.lifecycle.Observer
import com.ricardosfp.zapping.data.repository.zapping.ZappingRepository
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesHttpError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesOtherExceptionError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesParseError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesSuccess
import com.ricardosfp.zapping.data.repository.zapping.model.MyArticle
import com.ricardosfp.zapping.domain.match.MatchParser
import com.ricardosfp.zapping.domain.match.model.Match
import com.ricardosfp.zapping.domain.match.model.MatchParseSuccess
import com.ricardosfp.zapping.extension.InstantExecutorExtension
import com.ricardosfp.zapping.extension.MainDispatcherExtension
import com.ricardosfp.zapping.infrastructure.date.DateUtils
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiDataReady
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiError
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiIdle
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiLoading
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiState
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.LocalDate
import java.time.LocalDateTime

// todo do integration tests between the view model and the match parser
class ZappingViewModelTest {

    private lateinit var viewModel: ZappingViewModel
    private lateinit var zappingRepository: ZappingRepository
    private lateinit var dateUtils: DateUtils
    private lateinit var matchParser: MatchParser

    // todo find a way to inject this only for specific tests
    @RegisterExtension
    private val mainDispatcherExtension = MainDispatcherExtension()

    companion object {
        private val exception = Exception()

        private val repositoryOutput = GetArticlesSuccess(
            List(4) {
                MyArticle(
                    title = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1",
                    date = "Fri, 08 Nov 2024 14:45:00")
            })

        // this list is purposely unordered to check if the ViewModel orders the returned list
        private val matchParserOutputList = listOf(
            MatchParseSuccess(
                Match(
                    homeTeam = "Al Hilal",
                    awayTeam = "Al-Ettifaq",
                    date = LocalDateTime.of(2024, 11, 8, 14, 45),
                    channel = "SportTV 1",
                    originalText = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1")),
            MatchParseSuccess(
                Match(
                    homeTeam = "FC Vizela",
                    awayTeam = "GD Chaves",
                    date = LocalDateTime.of(2024, 11, 8, 18, 0),
                    channel = "SportTV +",
                    originalText = "FC Vizela x GD Chaves - 08/11 18:00 - SportTV +")),
            MatchParseSuccess(
                Match(
                    homeTeam = "Al-Riyadh",
                    awayTeam = "Al Nassr",
                    date = LocalDateTime.of(2024, 11, 8, 17, 0),
                    channel = "SportTV 1",
                    originalText = "Al-Riyadh x Al Nassr - 08/11 17:00 - SportTV 1")),
            MatchParseSuccess(
                Match(
                    homeTeam = "Al Hilal",
                    awayTeam = "Al-Ettifaq",
                    date = LocalDateTime.of(2024, 11, 7, 14, 45),
                    channel = "SportTV 1",
                    originalText = "Al Hilal x Al-Ettifaq - 08/11 14:45 - SportTV 1")))

        // this map is ordered. It is compared against the map returned by the ViewModel to check if it orders the returned map
        private val expectedUiDataReady = UiDataReady(
            mapOf(
                LocalDate.of(2024, 11, 7) to listOf(
                    matchParserOutputList[3].match
                ),
                LocalDate.of(2024, 11, 8) to listOf(
                    matchParserOutputList[0].match,
                    matchParserOutputList[2].match,
                    matchParserOutputList[1].match)
            ))
    }

    @BeforeEach
    fun setupInstance() {
        zappingRepository = mockk()
        dateUtils = mockk()
        matchParser = mockk()
        viewModel = ZappingViewModel(
            zappingRepository,
            dateUtils,
            matchParser,
            mainDispatcherExtension.testDispatcher)
    }

    @Test
    @DisplayName("check that the ZappingViewModel's initial UiState is UiIdle")
    fun checkInitialStateIsIdle() {
        assertEquals(UiIdle, viewModel.uiStateLiveData.value)
    }

    // this test checks that the return Map is ordered (its keys are ordered and the lists for the values are also ordered)
    @Test
    @ExtendWith(InstantExecutorExtension::class)
    @DisplayName("good repository response issues LiveData")
    fun goodRepositoryResponse_success() = runTest(mainDispatcherExtension.testDispatcher) {
        val observer = mockk<Observer<UiState>>()

        try {
            coEvery { zappingRepository.getArticles(any()) } returns repositoryOutput

            every { matchParser.parse(any()) } returnsMany matchParserOutputList

            val dateTime = slot<LocalDateTime>()
            every { dateUtils.getDate(capture(dateTime)) } answers { dateTime.captured.toLocalDate() }

            every { observer.onChanged(any()) } just runs

            viewModel.uiStateLiveData.observeForever(observer)

            viewModel.getMatches()

            // coVerifyOrder instead of coVerifySequence because the latter was catching toString() methods used by the debugger
            coVerifyOrder {
                observer.onChanged(UiIdle)
                observer.onChanged(UiLoading)
                zappingRepository.getArticles(any())
                repeat(repositoryOutput.articles.size) { i -> matchParser.parse(repositoryOutput.articles[i]) }
                observer.onChanged(any(UiDataReady::class))
            }

            // this is needed because the call above is not checking if the parser was called exactly x times
            verify(exactly = repositoryOutput.articles.size) { matchParser.parse(any()) }

            assertEquals(expectedUiDataReady, viewModel.uiStateLiveData.value)

            // this below is to check the order of the entries in the map
            assertEquals(
                expectedUiDataReady.dayMap.toList(),
                (viewModel.uiStateLiveData.value as UiDataReady).dayMap.toList(),
                "the returned map is not ordered")
        }
        finally {
            viewModel.uiStateLiveData.removeObserver(observer)
        }
    }

    @Nested
    inner class FailureTests {

        @Test
        @ExtendWith(InstantExecutorExtension::class)
        @DisplayName("http error from the repository issues UiError")
        fun getArticlesHttpError_uiError() = runTest(mainDispatcherExtension.testDispatcher) {
            val observer = mockk<Observer<UiState>>()

            try {
                coEvery { zappingRepository.getArticles(any()) } returns GetArticlesHttpError

                every { observer.onChanged(any()) } just runs

                viewModel.uiStateLiveData.observeForever(observer)

                viewModel.getMatches()

                coVerifyOrder {
                    observer.onChanged(UiIdle)
                    observer.onChanged(UiLoading)
                    zappingRepository.getArticles(any())
                    observer.onChanged(UiError)
                }

                verify(exactly = 0) { matchParser.parse(any()) }

                assertEquals(UiError, viewModel.uiStateLiveData.value)
            }
            finally {
                viewModel.uiStateLiveData.removeObserver(observer)
            }
        }


        @Test
        @ExtendWith(InstantExecutorExtension::class)
        @DisplayName("parse error from the repository issues UiError")
        fun getArticlesParseError_uiError() = runTest(mainDispatcherExtension.testDispatcher) {
            val observer = mockk<Observer<UiState>>()

            try {
                coEvery { zappingRepository.getArticles(any()) } returns GetArticlesParseError

                every { observer.onChanged(any()) } just runs

                viewModel.uiStateLiveData.observeForever(observer)

                viewModel.getMatches()

                coVerifyOrder {
                    observer.onChanged(UiIdle)
                    observer.onChanged(UiLoading)
                    zappingRepository.getArticles(any())
                    observer.onChanged(UiError)
                }

                verify(exactly = 0) { matchParser.parse(any()) }

                assertEquals(UiError, viewModel.uiStateLiveData.value)
            }
            finally {
                viewModel.uiStateLiveData.removeObserver(observer)
            }
        }

        @Test
        @ExtendWith(InstantExecutorExtension::class)
        @DisplayName("exception error from the repository issues UiError")
        fun getArticlesOtherExceptionError_uiError() =
            runTest(mainDispatcherExtension.testDispatcher) {
                val observer = mockk<Observer<UiState>>()

                try {
                    coEvery { zappingRepository.getArticles(any()) } returns
                            GetArticlesOtherExceptionError(exception)

                    every { observer.onChanged(any()) } just runs

                    viewModel.uiStateLiveData.observeForever(observer)

                    viewModel.getMatches()

                    coVerifyOrder {
                        observer.onChanged(UiIdle)
                        observer.onChanged(UiLoading)
                        zappingRepository.getArticles(any())
                        observer.onChanged(UiError)
                    }

                    verify(exactly = 0) { matchParser.parse(any()) }

                    assertEquals(UiError, viewModel.uiStateLiveData.value)
                }
                finally {
                    viewModel.uiStateLiveData.removeObserver(observer)
                }
            }
    }
}