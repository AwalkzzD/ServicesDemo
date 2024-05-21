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
            val notificationId = alarmBundle.getInt("NOTIFICATION_ID")
            val alarmType = alarmBundle.getString("ALARM_TYPE") ?: ""

            when (alarmType) {
                "PRE_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createPreAlarmNotification(alarmBundle, context)
                    )
                }

                "ON_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createAlarmNotification(alarmBundle, context)
                    )
                }

                "POST_ALARM" -> {
                    notificationManager.notify(
                        notificationId, createPostAlarmNotification(alarmBundle, context)
                    )
                }
            }
        }
    }
}