package com.ricardosfp.zapping.ui.viewmodel.zapping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardosfp.zapping.BuildConfig
import com.ricardosfp.zapping.data.repository.contract.ZappingRepository
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesError
import com.ricardosfp.zapping.data.repository.model.result.GetArticlesSuccess
import com.ricardosfp.zapping.domain.match.MatchParser
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.domain.model.MatchParseSuccess
import com.ricardosfp.zapping.infrastructure.alarm.MyAlarmManager
import com.ricardosfp.zapping.infrastructure.model.Alarm
import com.ricardosfp.zapping.infrastructure.util.date.DateUtils
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiDataReady
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiError
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiIdle
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiLoading
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

// todo test
@HiltViewModel
class ZappingViewModel @Inject constructor(
    private val zappingRepository: ZappingRepository,
    private val dateUtils: DateUtils,
    private val alarmManager: MyAlarmManager,
    private val matchParser: MatchParser
): ViewModel() {

    private val _uiStateLiveData = MutableLiveData<UiState>(UiIdle)
    val uiStateLiveData: LiveData<UiState> = _uiStateLiveData

    fun getMatches() {
        _uiStateLiveData.value = UiLoading
        viewModelScope.launch {
            val response = zappingRepository.getArticles(BuildConfig.ZAPPING_URL)

            withContext(Dispatchers.Main) {
                _uiStateLiveData.value = when (response) {
                    is GetArticlesSuccess -> {

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

                        val dayMap = mutableMapOf<Date, MutableList<Match>>()
                        sortedMatches.forEach { match ->
                            dayMap.computeIfAbsent(dateUtils.dateAtMidnight(match.date)) {
                                mutableListOf()
                            }.add(match)
                        }

                        UiDataReady(dayMap)
                    }

                    is GetArticlesError -> {
                        UiError
                    }
                }
            }
        }
    }

    fun scheduleAlarm(alarm: Alarm) {
        alarmManager.scheduleAlarm(alarm)
    }
}