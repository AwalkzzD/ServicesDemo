package com.example.servicesdemo.ui.home.alarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.servicesdemo.base.extensions.toLocalDateTime
import com.example.servicesdemo.base.utils.AlarmTimeType
import com.example.servicesdemo.data.dto.alarm.Alarm
import java.time.ZoneId

class AlarmScheduler(private val context: Context) {
    private var alarmManagerInstance: AlarmManager? = null

    private fun getAlarmManager(context: Context): AlarmManager =
        alarmManagerInstance ?: synchronized(Any()) {
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
                alarmManagerInstance = this
            }
        }

    fun schedulePreAlarm(alarmId: Int, alarm: Alarm) {
        val alarmBundle = Bundle().apply {
            putInt("ALARM_ID", alarmId)
            putInt("NOTIFICATION_ID", getAlarmId(alarmId, alarm, AlarmTimeType.PRE))
            putString("ALARM_TYPE", "PRE_ALARM")
            putString("MESSAGE", "${alarm.time} Alarm will ring soon")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP,
            getAlarmTime(alarm, AlarmTimeType.PRE),
            PendingIntent.getBroadcast(
                context,
                getAlarmId(alarmId, alarm, AlarmTimeType.PRE),
                Intent(context, AlarmReceiver::class.java).putExtras(alarmBundle),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun schedule(alarmId: Int, alarm: Alarm) {
        val alarmBundle = Bundle().apply {
            putInt("ALARM_ID", alarmId)
            putInt("NOTIFICATION_ID", getAlarmId(alarmId, alarm, AlarmTimeType.SCHEDULED))
            putString("ALARM_TYPE", "ON_ALARM")
            putString("MESSAGE", "${alarm.time} Alarm is ringing")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP,
            getAlarmTime(alarm, AlarmTimeType.SCHEDULED),
            PendingIntent.getBroadcast(
                context,
                getAlarmId(alarmId, alarm, AlarmTimeType.SCHEDULED),
                Intent(context, AlarmReceiver::class.java).putExtras(alarmBundle),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun schedulePostAlarm(alarmId: Int, alarm: Alarm) {

        val alarmBundle = Bundle().apply {
            putInt("ALARM_ID", alarmId)
            putInt("NOTIFICATION_ID", getAlarmId(alarmId, alarm, AlarmTimeType.POST))
            putString("ALARM_TYPE", "POST_ALARM")
            putString("MESSAGE", "${alarm.time} Alarm rang few minutes ago")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP,
            getAlarmTime(alarm, AlarmTimeType.POST),
            PendingIntent.getBroadcast(
                context,
                getAlarmId(alarmId, alarm, AlarmTimeType.POST),
                Intent(context, AlarmReceiver::class.java).putExtras(alarmBundle),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(alarmId: Int) {
        getAlarmManager(context).cancel(
            PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmDismissReceiver::class.java).putExtras(Bundle()),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun getAlarmTime(alarm: Alarm, timeType: AlarmTimeType): Long = when (timeType) {
        AlarmTimeType.PRE -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L) - (3 * 60 * 1000)
        }

        AlarmTimeType.SCHEDULED -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L)
        }

        AlarmTimeType.POST -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L) + (3 * 60 * 1000)
        }
    }

    private fun getAlarmId(alarmId: Int, alarm: Alarm, timeType: AlarmTimeType): Int =
        when (timeType) {
            AlarmTimeType.PRE -> {
                (alarm.time.replace(":", "") + alarmId.toString() + "100").toInt()
            }

            AlarmTimeType.SCHEDULED -> {
                (alarm.time.replace(":", "") + alarmId.toString() + "010").toInt()
            }

            AlarmTimeType.POST -> {
                (alarm.time.replace(":", "") + alarmId.toString() + "001").toInt()
            }
        }

}