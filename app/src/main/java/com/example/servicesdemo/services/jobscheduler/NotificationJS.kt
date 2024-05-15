package com.example.servicesdemo.services.jobscheduler

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import androidx.work.Configuration
import com.example.servicesdemo.base.utils.NotificationUtils

class NotificationJS(
    var a: Configuration = Configuration.Builder().setJobSchedulerJobIdRange(0, 1000).build()
) : JobService() {

    private val notificationManager: NotificationManager by lazy {
        NotificationUtils.getNotificationManager(applicationContext)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        sendJSNotification()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun sendJSNotification() {
        notificationManager.notify(
            2, NotificationUtils.createJSNotification(applicationContext, "JS Notification")
        )
    }
}