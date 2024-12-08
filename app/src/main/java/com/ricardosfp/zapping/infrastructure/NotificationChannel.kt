package com.ricardosfp.zapping.infrastructure

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.ricardosfp.zapping.R

enum class NotificationChannel(
    val id: String, private val categoryName: Int, private val description: Int, val importance: Int
) {
    MATCH(
        "0",
        R.string.notification_match_name,
        R.string.notification_match_description,
        NotificationManagerCompat.IMPORTANCE_DEFAULT);

    fun getName(context: Context): String {
        return context.getString(categoryName)
    }

    fun getDescription(context: Context): String {
        return context.getString(description)
    }
}