package com.example.servicesdemo.ui.home.alarm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.servicesdemo.base.utils.NotificationUtils
import com.example.servicesdemo.services.foreground.AlarmService

private const val TAG = "AlarmDismissHandler"

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 999)
        val alarmAction = intent.getStringExtra("ALARM_ACTION") ?: ""
        Log.e(TAG, "onReceive: NOTIFICATION ID $notificationId", null)
        NotificationUtils.cancelNotification(context, notificationId)

        if (alarmAction.isNotBlank()) {
            context.startService(
                Intent(context, AlarmService::class.java).putExtra(
                    "ALARM_ACTION", alarmAction
                )
            )
        } else {
            Toast.makeText(context, "Could not perform any DB actions", Toast.LENGTH_SHORT).show()
        }

    }
}