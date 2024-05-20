package com.example.servicesdemo.services.foreground

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.servicesdemo.base.utils.AlarmsAppDatabase
import com.example.servicesdemo.ui.home.alarm.utils.AlarmScheduler

private const val TAG = "AlarmService"

class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmBundle = intent?.extras

        if (alarmBundle != null) {
            val alarmId = alarmBundle.getInt("ALARM_ID")
            val hashcode = alarmBundle.getInt("NOTIFICATION_ID")
            val alarmAction = alarmBundle.getString("ALARM_ACTION") ?: ""

            if (alarmAction.isNotBlank()) {
                performDbOperation(alarmId, alarmAction, hashcode)
            } else {
                Log.d(TAG, "onStartCommand: alarm action is blank")
            }
        }
        return START_STICKY
    }

    private fun performDbOperation(alarmId: Int, alarmAction: String, hashcode: Int) {

        val alarmsDao = AlarmsAppDatabase.getInstance(this).alarmsDao()

        // Perform database operation based on the alarm action
        when (alarmAction) {
            "PRE_ALARM" -> {
                AlarmScheduler(this).cancel((hashcode.toString().replace("100", "010")).toInt())
                AlarmScheduler(this).cancel((hashcode.toString().replace("100", "001")).toInt())
                alarmsDao.updateAlarmPreAction(alarmId, true)
            }

            "ON_ALARM" -> {
                Log.d(TAG, "performDbOperation: ON ALARM")
                AlarmScheduler(this).cancel((hashcode.toString().replace("010", "001")).toInt())
                alarmsDao.updateAlarmOnAction(alarmId, true)
            }

            "POST_ALARM" -> {
                AlarmScheduler(this).cancel(hashcode)
                Log.d(TAG, "performDbOperation: POST ALARM")
                alarmsDao.updateAlarmPostAction(alarmId, true)
            }
        }
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()
    }
}