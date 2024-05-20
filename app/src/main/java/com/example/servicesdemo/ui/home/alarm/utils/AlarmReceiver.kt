package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.servicesdemo.base.utils.NotificationUtils.createAlarmNotification
import com.example.servicesdemo.base.utils.NotificationUtils.createPostAlarmNotification
import com.example.servicesdemo.base.utils.NotificationUtils.createPreAlarmNotification
import com.example.servicesdemo.base.utils.NotificationUtils.getNotificationManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmBundle = intent.extras
        val notificationManager = getNotificationManager(context)

        if (alarmBundle != null) {
            val alarmId = alarmBundle.getInt("ALARM_ID")
            val notificationId = alarmBundle.getInt("NOTIFICATION_ID")
            val alarmType = alarmBundle.getString("ALARM_TYPE") ?: ""
            val message = alarmBundle.getString("MESSAGE") ?: ""

            when (alarmType) {
                "PRE_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createPreAlarmNotification(
                            alarmId, notificationId, message, context
                        )
                    )
                }

                "ON_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createAlarmNotification(
                            alarmId, notificationId, message, context
                        )
                    )
                }

                "POST_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createPostAlarmNotification(
                            alarmId, notificationId, message, context
                        )
                    )
                }
            }
        }
    }
}