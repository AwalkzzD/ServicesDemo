package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.servicesdemo.base.utils.NotificationUtils

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("TAG", "receiver: called")

        val alarmBundle = intent.extras
        val notificationManager = NotificationUtils.getNotificationManager(context)

        if (alarmBundle != null) {

            val message = alarmBundle.getString("EXTRA_MESSAGE") ?: ""
            val notificationId = alarmBundle.getInt("NOTIFICATION_ID")
            val alarmType = alarmBundle.getString("ALARM_TYPE") ?: ""
            Log.d("TAG", "onReceive: $alarmType - $message")

            when (alarmType) {
                "PRE_ALARM" -> {
                    notificationManager.notify(
                        notificationId, NotificationUtils.createPreAlarmNotification(
                            context, message, notificationId
                        )
                    )
                }

                "ON_ALARM" -> {
                    notificationManager.notify(
                        notificationId, NotificationUtils.createAlarmNotification(
                            context, message, notificationId
                        )
                    )
                }

                "POST_ALARM" -> {
                    notificationManager.notify(
                        notificationId, NotificationUtils.createPostAlarmNotification(
                            context, message, notificationId
                        )
                    )
                }

            }
        }
    }
}