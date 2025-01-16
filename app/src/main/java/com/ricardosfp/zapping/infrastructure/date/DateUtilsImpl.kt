package com.ricardosfp.zapping.infrastructure.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

// todo test this. But, is there really something to test?
// todo are the parse and format functions really necessary?
@Singleton
class DateUtilsImpl @Inject constructor(): DateUtils {

    override fun parse(dateString: String, pattern: String, locale: Locale): LocalDateTime =
        LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern, locale))

    override fun format(date: LocalDateTime, pattern: String, locale: Locale): String =
        date.format(DateTimeFormatter.ofPattern(pattern, locale))

    override fun getDate(date: LocalDateTime): LocalDate = date.toLocalDate()
}