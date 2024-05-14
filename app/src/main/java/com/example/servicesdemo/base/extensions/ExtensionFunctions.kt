package com.example.servicesdemo.base.extensions

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun <T> Context.isServiceRunning(service: Class<T>) =
    (getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
        Integer.MAX_VALUE
    ).any { it.service.className == service.name }

fun Long.convertToTime(): String {
    val df = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("GMT")
    return df.format(Date(this))
}