package com.ricardosfp.zapping.infrastructure.model

import java.io.Serializable
import java.time.LocalDateTime

class Alarm(val matchText: String, val matchDate: LocalDateTime): Serializable