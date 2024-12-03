package com.ricardosfp.zapping.domain.model

import java.text.*

sealed class MatchParseResult

data class MatchParseSuccess(val match: Match): MatchParseResult()

sealed class MatchParseError: MatchParseResult()

data class MatchParseDateError(val exception: ParseException?): MatchParseError()

data object MatchParseTitleError: MatchParseError()

data class MatchParseOtherExceptionError(val exception: Exception): MatchParseError()