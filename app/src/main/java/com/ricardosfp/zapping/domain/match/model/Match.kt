package com.ricardosfp.zapping.domain.match.model

import java.io.Serializable
import java.time.LocalDateTime

// todo does this have to be Serializable?
data class Match(
    val homeTeam: String,
    val awayTeam: String,
    val date: LocalDateTime,
    val channel: String,
    val originalText: String
): Serializable