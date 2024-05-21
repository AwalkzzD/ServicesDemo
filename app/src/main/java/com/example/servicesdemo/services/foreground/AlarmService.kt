package com.example.servicesdemo.services.foreground

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.example.servicesdemo.base.utils.AlarmsAppDatabase
import com.example.servicesdemo.ui.home.alarm.utils.AlarmScheduler

private const val TAG = "AlarmService"

class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmBundle = intent?.extras

        if (alarmBundle != null) {
            performDbOperation(alarmBundle)
        }
        return START_STICKY
    }

    private fun performDbOperation(alarmBundle: Bundle) {

        val alarmsDao = AlarmsAppDatabase.getInstance(this).alarmsDao()

        val alarmId = alarmBundle.getInt("ALARM_ID")
        val hashcode = alarmBundle.getInt("NOTIFICATION_ID")
        val alarmType = alarmBundle.getString("ALARM_TYPE") ?: ""

        // Perform database operation based on the alarm action
        when (alarmType) {
            "PRE_ALARM" -> {
                AlarmScheduler(applicationContext).cancel(
                    (hashcode.toString().replace("100", "010")).toInt(), alarmBundle
                )
                AlarmScheduler(applicationContext).cancel(
                    (hashcode.toString().replace("100", "001")).toInt(), alarmBundle
                )
                alarmsDao.updateAlarmPreAction(alarmId, true)
            }

            "ON_ALARM" -> {
                AlarmScheduler(applicationContext).cancel(
                    (hashcode.toString().replace("010", "001")).toInt(), alarmBundle
                )
                alarmsDao.updateAlarmOnAction(alarmId, true)
            }

            "POST_ALARM" -> {
                AlarmScheduler(applicationContext).cancel(hashcode, alarmBundle)
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