package com.ricardosfp.zapping.infrastructure.alarm

import com.ricardosfp.zapping.infrastructure.model.*

// I added the "My" to avoid name collision with [android.app.AlarmManager]
interface MyAlarmManager {

    companion object {
        const val BUNDLE_KEY = "alarm_object"
        const val RECEIVER_ACTION = "match.alarm"
    }

    fun scheduleAlarm(alarm: Alarm)
}