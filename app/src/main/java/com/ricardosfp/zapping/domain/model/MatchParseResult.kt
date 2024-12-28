package com.ricardosfp.zapping.domain.model

import java.time.format.DateTimeParseException

sealed class MatchParseResult

data class MatchParseSuccess(val match: Match): MatchParseResult()

sealed class MatchParseError: MatchParseResult()

// to be even more library agnostic I could create my own exceptions
data class MatchParseDateError(val exception: DateTimeParseException): MatchParseError()

data object MatchParseTitleError: MatchParseError()

data class MatchParseOtherExceptionError(val exception: Exception): MatchParseError()