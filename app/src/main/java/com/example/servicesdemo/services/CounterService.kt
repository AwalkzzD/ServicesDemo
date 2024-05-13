package com.example.servicesdemo.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.servicesdemo.base.utils.NotificationUtils
import com.example.servicesdemo.base.utils.NotificationUtils.createNotification
import java.util.Timer
import java.util.TimerTask

class CounterService : Service() {

    private val notificationManager: NotificationManager =
        NotificationUtils.getNotificationManager(this@CounterService)

    private var counter = 0
    private val timer = Timer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification(this@CounterService, counter))

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (counter < 100) {
                    counter++

                    notificationManager.notify(1, createNotification(this@CounterService, counter))

                    val callBackIntent = Intent("counter_updated")
                    callBackIntent.putExtra("counter", counter)
                    sendBroadcast(callBackIntent)
                } else {
                    stopSelf()
                }
            }
        }, 0, 1000)

        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        timer.cancel()
        super.onDestroy()
    }
}