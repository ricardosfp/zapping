package com.ricardosfp.zapping.ui.viewmodel.zapping

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardosfp.zapping.BuildConfig
import com.ricardosfp.zapping.data.repository.zapping.ZappingRepository
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesError
import com.ricardosfp.zapping.data.repository.zapping.model.GetArticlesSuccess
import com.ricardosfp.zapping.domain.match.MatchParser
import com.ricardosfp.zapping.domain.match.model.Match
import com.ricardosfp.zapping.domain.match.model.MatchParseSuccess
import com.ricardosfp.zapping.infrastructure.date.DateUtils
import com.ricardosfp.zapping.infrastructure.di.DispatcherModule.DefaultDispatcher
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiDataReady
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiError
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiIdle
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiLoading
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ZappingViewModel @Inject constructor(
    private val zappingRepository: ZappingRepository,
    private val dateUtils: DateUtils,
    private val matchParser: MatchParser,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {

    companion object {
        private const val DATE_FORMAT = "EEEE, MMM d"

        // the formatting is not respecting the device's language
        // todo this should depend on the context, to return a localized string
        private val LOCALE = Locale.ENGLISH
    }

    private val _uiStateLiveData = MutableLiveData<UiState>(UiIdle)
    val uiStateLiveData: LiveData<UiState> = _uiStateLiveData

    @MainThread
    fun getMatches() {
        _uiStateLiveData.value = UiLoading
        viewModelScope.launch {
            val response = zappingRepository.getArticles(BuildConfig.ZAPPING_URL)

            withContext(Dispatchers.Main) {
                _uiStateLiveData.value = when (response) {
                    is GetArticlesSuccess -> {

                        withContext(defaultDispatcher) {
                            // todo this should be done in a future Use Case, not here, to avoid calling the repository,
                            //  getting a response and then calling the domain layer
                            val matches = response.articles.mapNotNull {
                                val matchParseResult = matchParser.parse(it)

                                if (matchParseResult is MatchParseSuccess) {
                                    matchParseResult.match
                                } else null
                            }

                            // order matches by date. Do not assume that they come ordered
                            // if we order the list of matches then we do not need to order the map
                            // it is simpler this way
                            val sortedMatches = matches.sortedBy {
                                it.date
                            }

                            val dayMap = mutableMapOf<LocalDate, MutableList<Match>>()
                            sortedMatches.forEach { match ->
                                dayMap.computeIfAbsent(
                                    dateUtils.getDate(match.date)) {
                                    mutableListOf()
                                }.add(match)
                            }
                            UiDataReady(dayMap)
                        }
                    }

                    is GetArticlesError -> {
                        UiError
                    }
                }
            }
        }
    }

    fun getFormattedDateString(date: LocalDate) = dateUtils.format(date, DATE_FORMAT, LOCALE)

}