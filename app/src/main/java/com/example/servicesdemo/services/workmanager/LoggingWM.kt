package com.example.servicesdemo.services.workmanager

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.servicesdemo.base.utils.NotificationUtils

private const val TAG = "LoggingWM"

class LoggingWM(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val notificationManager: NotificationManager by lazy {
        NotificationUtils.getNotificationManager(context)
    }

    override fun doWork(): Result {
        Log.d(TAG, "doWork: Logging chain task started")

        notificationManager.notify(
            3, NotificationUtils.createWMNotification(
                applicationContext, "WM Logging Chain Notification"
            )
        )

        return Result.success()
    }
}