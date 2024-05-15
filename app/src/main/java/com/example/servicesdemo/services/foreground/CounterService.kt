package com.example.servicesdemo.services.foreground

import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.example.servicesdemo.R
import com.example.servicesdemo.base.extensions.convertToTime
import com.example.servicesdemo.base.utils.NotificationUtils
import com.example.servicesdemo.base.utils.NotificationUtils.createNotification
import java.util.Timer
import java.util.TimerTask

class CounterService : Service() {

    private val timer = Timer()
    private val notificationManager: NotificationManager by lazy {
        NotificationUtils.getNotificationManager(this@CounterService)
    }

    private lateinit var broadcastReceiver: BroadcastReceiver

    private var counter = 0
    private var countDownTime: Long = 10000

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    val timeDiff = (intent.getLongExtra("countdown_time", 0) * 1000)
                    countDownTime += timeDiff

                    Toast.makeText(
                        context,
                        "Task will run for more ${timeDiff.convertToTime()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                broadcastReceiver, IntentFilter("timer_updated"), Context.RECEIVER_EXPORTED
            )
        }

        startForeground(1, createNotification(this@CounterService, countDownTime.convertToTime()))

        performCountDown()

        return START_STICKY
    }

    private fun performCountDown() {
        Toast.makeText(
            this, "Task will run for ${countDownTime.convertToTime()}", Toast.LENGTH_SHORT
        ).show()

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (countDownTime / 1000 > 0) {
                    counter++
                    countDownTime -= 1000

                    notificationManager.notify(
                        1, createNotification(this@CounterService, countDownTime.convertToTime())
                    )

                    sendBroadcast(Intent("counter_updated").putExtra("counter", counter))
                } else {
                    /**
                     *  To stop the service after the countdown value is over,
                     *  Once service is stopped, it will not show the notification and the counter will not be updated.
                     */
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        }, 0, 1000)
    }

    override fun onDestroy() {
        Toast.makeText(this, getString(R.string.service_stopped_toast_msg), Toast.LENGTH_SHORT)
            .show()
        unregisterReceiver(broadcastReceiver)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        timer.cancel()
        super.onDestroy()
    }
}