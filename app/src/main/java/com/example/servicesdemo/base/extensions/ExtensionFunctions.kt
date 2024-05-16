package com.example.servicesdemo.base.extensions

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.example.servicesdemo.data.local.model.AlarmEntity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
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

fun <T> List<T>.toLiveData(): LiveData<List<T>> {
    val liveData = MutableLiveData<List<T>>()
    liveData.value = this
    return liveData
}

fun AlarmEntity.toAlarm(): Alarm = Alarm(
    id = id, time = time, message = message
)

fun Alarm.toAlarmEntity(): AlarmEntity = AlarmEntity(
    id = id,
    time = time,
    message = message,
    preAlarmAction = false,
    onAlarmAction = false,
    postAlarmAction = false
)

fun String.toLocalDateTime(): LocalDateTime {
    val time = LocalTime.parse(this)
    return LocalDateTime.now().withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0)
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId()
    )
}