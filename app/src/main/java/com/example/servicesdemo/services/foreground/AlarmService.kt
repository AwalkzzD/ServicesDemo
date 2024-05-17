package com.example.servicesdemo.services.foreground

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.servicesdemo.R
import com.example.servicesdemo.base.utils.AlarmsAppDatabase

class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmAction = intent?.getStringExtra("ALARM_ACTION") ?: ""
        if (alarmAction.isNotEmpty()) {
            performDbOperation(alarmAction)
        }
        return START_STICKY
    }

    private fun performDbOperation(alarmAction: String) {

        val alarmsDao = AlarmsAppDatabase.getInstance(this).alarmsDao()

        // Perform database operation based on the alarm action
        when (alarmAction) {
            "PRE_ALARM" -> {
                Log.d("TAG", "performDbOperation: ${alarmsDao.getAllAlarms()}")
            }

            "ON_ALARM" -> {

            }

            "POST_ALARM" -> {

            }
        }
    }

    override fun onDestroy() {
        Toast.makeText(this, getString(R.string.service_stopped_toast_msg), Toast.LENGTH_SHORT)
            .show()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()
    }
}