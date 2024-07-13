package com.ricardosfp.zapping.ui.viewmodel

import androidx.lifecycle.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.domain.match.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.infrastructure.alarm.*
import com.ricardosfp.zapping.infrastructure.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.*

@HiltViewModel
class ZappingViewModel @Inject constructor(
    private val zappingRepository: ZappingRepository,
    private val dateUtils: DateUtils,
    private val alarmManager: MyAlarmManager,
    private val matchParser: MatchParser
): ViewModel() {

    private val _matchesLiveData = MutableLiveData<GetMatchesResponse>()
    val matchesLiveData: LiveData<GetMatchesResponse> = _matchesLiveData

    fun getMatches() {
        viewModelScope.launch {
            val response = zappingRepository.getMatches()

            withContext(Dispatchers.Main) {
                _matchesLiveData.value = when (response) {
                    is GetMatchesRepositoryResponseSuccess -> {

                        // this should be done in a future Use Case, not here
                        val matches = response.articles.mapNotNull {
                            val matchParseResult = matchParser.parse(it)

                            if (matchParseResult is MatchParseSuccess) {
                                matchParseResult.match
                            } else null
                        }

                        val dayMap = TreeMap<Date, ArrayList<Match>>()
                        matches.forEach { match ->
                            dayMap.computeIfAbsent(dateUtils.dateAtMidnight(match.date)) {
                                ArrayList()
                            }.add(match)
                        }

                        GetMatchesResponseSuccess(dayMap)
                    }

                    is GetMatchesRepositoryResponseError -> {
                        GetMatchesResponseError(response.throwable)
                    }
                }
            }
        }
    }

    fun scheduleAlarm(alarm: Alarm) {
        alarmManager.scheduleAlarm(alarm)
    }
}

sealed class GetMatchesResponse
class GetMatchesResponseSuccess(val dayMap: TreeMap<Date, ArrayList<Match>>): GetMatchesResponse()
class GetMatchesResponseError(val throwable: Throwable): GetMatchesResponse()