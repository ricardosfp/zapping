package com.ricardosfp.zapping.domain.model

import java.io.*
import java.util.*

// todo does this have to be Serializable?
data class Match(
    val homeTeam: String,
    val awayTeam: String,
    val date: Date,
    val channel: String,
    val originalText: String
): Serializable