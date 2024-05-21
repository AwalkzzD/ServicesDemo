package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.servicesdemo.base.utils.NotificationUtils.cancelNotification
import com.example.servicesdemo.services.foreground.AlarmService

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmBundle = intent.extras

        if (alarmBundle != null) {
            val notificationId = alarmBundle.getInt("NOTIFICATION_ID")
            cancelNotification(context, notificationId)

            context.startService(
                Intent(context, AlarmService::class.java).putExtras(alarmBundle)
            )
        } else {
            Toast.makeText(context, "Could not perform any DB actions", Toast.LENGTH_SHORT).show()
        }

    }
}