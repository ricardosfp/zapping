package com.ricardosfp.zapping.ui.viewmodel.zapping.model

import com.ricardosfp.zapping.domain.model.*
import java.util.*

sealed class GetMatchesResult

data class GetMatchesSuccess(val dayMap: TreeMap<Date, ArrayList<Match>>): GetMatchesResult()

data object GetMatchesError: GetMatchesResult()