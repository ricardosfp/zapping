package com.ricardosfp.zapping.infrastructure.util.date

import java.text.*
import java.util.*

interface DateUtils {
    fun parse(dateFormat: SimpleDateFormat, dateStringToParse: String): Date?

    fun format(dateFormat: SimpleDateFormat, date: Date): String

    fun dateAtMidnight(date: Date): Date
}