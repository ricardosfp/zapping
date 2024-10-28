package com.ricardosfp.zapping.domain.model

sealed class MatchParseResult

data class MatchParseSuccess(val match: Match): MatchParseResult()

sealed class MatchParseError: MatchParseResult()

// this should be specified as a ParseException
data class MatchParseDateError(val exception: Exception): MatchParseError()

data object MatchParseTitleError: MatchParseError()

// MatchParseUnknownExceptionError, because there is another error with exceptions
data class MatchParseExceptionError(val exception: Exception): MatchParseError()

data object MatchParseUnknownError: MatchParseError()