package com.ricardosfp.zapping.domain.match

import com.ricardosfp.zapping.domain.model.*

sealed class MatchParseResult

class MatchParseSuccess(val match: Match): MatchParseResult()

sealed class MatchParseError: MatchParseResult()

class MatchParseDateError(val exception: Exception): MatchParseError()

object MatchParseTitleError: MatchParseError()

class MatchParseExceptionError(val exception: Exception): MatchParseError()

object MatchParseUnknownError: MatchParseError()