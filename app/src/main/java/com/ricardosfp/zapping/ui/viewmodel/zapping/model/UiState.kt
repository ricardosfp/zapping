package com.ricardosfp.zapping.ui.viewmodel.zapping.model

import com.ricardosfp.zapping.domain.match.model.Match
import java.util.Collections

sealed class UiState

data object UiIdle: UiState()

data object UiLoading: UiState()

// todo this should receive a LinkedHashMap, to guarantee key order
// todo this does not stop someone from casting a list of matches to modify it
// todo this is not optimal at all because it still holds two "equal" objects
data class UiDataReady(private val dayMapParameter: Map<DateWithFormattedString, List<Match>>):
    UiState() {
    val dayMap: Map<DateWithFormattedString, List<Match>> = Collections.unmodifiableMap(
        dayMapParameter)
}

data object UiError: UiState()