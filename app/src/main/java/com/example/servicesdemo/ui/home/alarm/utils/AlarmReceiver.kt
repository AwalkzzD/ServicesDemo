package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.servicesdemo.base.utils.NotificationUtils
import com.example.servicesdemo.base.utils.NotificationUtils.createPreAlarmNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: ""
        val notificationManager = NotificationUtils.getNotificationManager(context)
        notificationManager.notify(
            12,
            createPreAlarmNotification(context, message)
        )
    }
}