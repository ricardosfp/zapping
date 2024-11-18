package com.ricardosfp.zapping.ui.viewmodel.zapping.model

import com.ricardosfp.zapping.domain.model.*
import java.util.*

sealed class GetMatchesResult

// this uses a MutableMap to guarantee key order
// todo this does not stop someone from modifying a Date, or from casting a list of matches to modify it
// todo this is not optimal at all because it still holds two "equal" objects
data class GetMatchesSuccess(private val dayMapParameter: MutableMap<Date, MutableList<Match>>):
    GetMatchesResult() {
    val dayMap: Map<Date, List<Match>> = Collections.unmodifiableMap(dayMapParameter)
}

data object GetMatchesError: GetMatchesResult()