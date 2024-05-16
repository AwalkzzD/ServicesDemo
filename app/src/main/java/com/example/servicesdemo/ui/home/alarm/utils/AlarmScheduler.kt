package com.example.servicesdemo.ui.home.alarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.servicesdemo.base.extensions.toLocalDateTime
import com.example.servicesdemo.base.utils.CommonUtils
import com.example.servicesdemo.data.dto.alarm.Alarm
import java.time.ZoneId

private const val TAG = "AlarmScheduler"

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val intent = Intent(context, AlarmReceiver::class.java)

    private fun schedulePreAlarm(alarm: Alarm) {
        val alarmTime =
            alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L - (3 * 60 * 1000)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode() + 1,
                intent.putExtra("EXTRA_MESSAGE", "${alarm.time} Alarm will ring soon"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun schedule(alarm: Alarm) {
        if (!CommonUtils.checkPreAlarmTimePassed(alarm.time.toLocalDateTime())) {
            schedulePreAlarm(alarm)
        }
        schedulePostAlarm(alarm)

        val alarmTime =
            alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode() + 2,
                intent.putExtra("EXTRA_MESSAGE", "${alarm.time} Alarm is ringing"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun schedulePostAlarm(alarm: Alarm) {
        val alarmTime =
            alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L + (3 * 60 * 1000)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode() + 3,
                intent.putExtra("EXTRA_MESSAGE", "${alarm.time} Alarm rang 3 minutes ago"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(alarm: Alarm) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}