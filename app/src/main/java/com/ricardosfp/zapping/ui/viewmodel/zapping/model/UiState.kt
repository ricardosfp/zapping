package com.ricardosfp.zapping.ui.viewmodel.zapping.model

import com.ricardosfp.zapping.domain.model.Match
import java.util.Collections
import java.util.Date

sealed class UiState

data object UiIdle: UiState()

data object UiLoading: UiState()

// todo this should receive a LinkedHashMap, to guarantee key order
// todo this does not stop someone from modifying a Date, or from casting a list of matches to modify it
// todo this is not optimal at all because it still holds two "equal" objects
data class UiDataReady(private val dayMapParameter: MutableMap<Date, MutableList<Match>>):
    UiState() {
    val dayMap: Map<Date, List<Match>> = Collections.unmodifiableMap(dayMapParameter)
}

data object UiError: UiState()