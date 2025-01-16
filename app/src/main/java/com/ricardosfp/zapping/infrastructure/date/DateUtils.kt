package com.ricardosfp.zapping.infrastructure.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

// to be even more library agnostic I could create my own date and time classes
// and creating functions for building instances
interface DateUtils {
    fun parse(dateString: String, pattern: String, locale: Locale): LocalDateTime

    fun format(date: LocalDateTime, pattern: String, locale: Locale): String

    fun getDate(date: LocalDateTime): LocalDate
}