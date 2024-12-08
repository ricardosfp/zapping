package com.ricardosfp.zapping.infrastructure.util.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

// todo test this. But, is there really something to test?
// todo are the parse and format functions really necessary?
@Singleton
class DateUtilsImpl @Inject constructor(): DateUtils {

    // use something that does not return null
    override fun parse(dateFormat: SimpleDateFormat, dateStringToParse: String): Date? {
        return dateFormat.parse(dateStringToParse)
    }

    override fun format(dateFormat: SimpleDateFormat, date: Date): String {
        return dateFormat.format(date)
    }

    override fun dateAtMidnight(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }
}