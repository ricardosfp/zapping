package com.ricardosfp.zapping.ui.viewmodel.zapping

import androidx.lifecycle.*
import com.ricardosfp.zapping.*
import com.ricardosfp.zapping.data.repository.contract.*
import com.ricardosfp.zapping.data.repository.model.*
import com.ricardosfp.zapping.domain.match.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.infrastructure.alarm.*
import com.ricardosfp.zapping.infrastructure.model.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.*
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

    private val _matchesLiveData = MutableLiveData<GetMatchesResult>()
    val matchesLiveData: LiveData<GetMatchesResult> = _matchesLiveData

    fun getMatches() {
        viewModelScope.launch {
            val response = zappingRepository.getArticles(BuildConfig.ZAPPING_URL)

            withContext(Dispatchers.Main) {
                _matchesLiveData.value = when (response) {
                    is GetArticlesSuccess -> {

                        // todo this should be done in a future Use Case, not here, to avoid calling the repository,
                        //  getting a response and then calling the domain layer
                        val matches = response.articles.mapNotNull {
                            val matchParseResult = matchParser.parse(it)

                            if (matchParseResult is MatchParseSuccess) {
                                matchParseResult.match
                            } else null
                        }

                        // todo matches should be ordered in crescent order. We have no guarantee that they come ordered

                        val dayMap = TreeMap<Date, ArrayList<Match>>()
                        matches.forEach { match ->
                            dayMap.computeIfAbsent(dateUtils.dateAtMidnight(match.date)) {
                                ArrayList()
                            }.add(match)
                        }

                        GetMatchesSuccess(dayMap)
                    }

                    is GetArticlesError -> {
                        GetMatchesError
                    }
                }
            }
        }
    }

    fun scheduleAlarm(alarm: Alarm) {
        alarmManager.scheduleAlarm(alarm)
    }
}