package com.example.servicesdemo.services.workmanager

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.servicesdemo.base.utils.NotificationUtils

private const val TAG = "NotificationWM"

class NotificationWM(private val context: Context, workerParams: WorkerParameters) : Worker(
    context, workerParams
) {

    private val data: String = workerParams.inputData.getString("name").toString()

    private val notificationManager: NotificationManager by lazy {
        NotificationUtils.getNotificationManager(context)
    }

    override fun doWork(): Result {

        Log.d(TAG, "doWork: Notification chain task started")

        notificationManager.notify(
            2, NotificationUtils.createWMNotification(context, "WM Notification $data")
        )

        return Result.success()
    }
}