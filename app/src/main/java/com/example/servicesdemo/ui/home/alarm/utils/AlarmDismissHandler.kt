package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.servicesdemo.base.utils.NotificationUtils

class AlarmDismissHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notification_id", -1)
        NotificationUtils.cancelNotification(context, notificationId)
    }
}