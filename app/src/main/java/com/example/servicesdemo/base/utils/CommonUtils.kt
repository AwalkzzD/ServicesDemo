package com.example.servicesdemo.base.utils

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.servicesdemo.R
import com.example.servicesdemo.base.extensions.toLocalDateTime
import com.example.servicesdemo.base.utils.AlarmTimeType.POST
import com.example.servicesdemo.base.utils.AlarmTimeType.PRE
import com.example.servicesdemo.base.utils.AlarmTimeType.SCHEDULED
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_ALARM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_JS
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_WM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_ALARM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_JS
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_WM
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.example.servicesdemo.ui.home.MainActivity
import com.example.servicesdemo.ui.home.alarm.utils.AlarmDismissReceiver
import com.example.servicesdemo.ui.home.alarm.utils.AlarmReceiver
import java.time.LocalDateTime
import java.time.ZoneId


/**
 * Dialog Utils
 */
object DialogUtils {

    fun getProgressDialog(context: Context): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(R.layout.progress_dialog)
        dialogBuilder.setCancelable(false)
        return dialogBuilder.create()
            .apply { window?.setBackgroundDrawableResource(android.R.color.transparent) }
    }

    fun getAlertDialog(
        titleText: String,
        messageText: String,
        positiveButtonText: String,
        onPositiveClick: () -> Unit,
        isCancelable: Boolean,
        context: Context
    ): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(messageText).setCancelable(isCancelable)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositiveClick()
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(titleText)

        return alert
    }
}

/**
 * Notification Utils
 */
object NotificationUtils {

    fun getNotificationManager(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotification(context: Context, msg: String): Notification {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

        getNotificationManager(context).createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Counter Service")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentIntent(pendingIntent)
            .setOngoing(true).build()
    }

    fun createWMNotification(context: Context, msg: String): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID_WM, CHANNEL_NAME_WM, NotificationManager.IMPORTANCE_DEFAULT
        )

        getNotificationManager(context).createNotificationChannel(channel)

        return NotificationCompat.Builder(context, CHANNEL_ID_WM)
            .setContentTitle("Work Manager Test").setPriority(NotificationManager.IMPORTANCE_MAX)
            .setContentText(msg).setSmallIcon(R.drawable.ic_launcher_foreground).setOngoing(true)
            .setAutoCancel(true).build()
    }

    fun createJSNotification(context: Context, msg: String): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID_JS, CHANNEL_NAME_JS, NotificationManager.IMPORTANCE_DEFAULT
        )

        getNotificationManager(context).createNotificationChannel(channel)


        return NotificationCompat.Builder(context, CHANNEL_ID_JS)
            .setContentTitle("Job Scheduler Test").setPriority(NotificationManager.IMPORTANCE_MAX)
            .setContentText(msg).setSmallIcon(R.drawable.ic_launcher_foreground).setOngoing(true)
            .setAutoCancel(true).build()
    }

    fun createPreAlarmNotification(
        context: Context, msg: String, notificationId: Int
    ): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true).addAction(
                0, "CANCEL ALARM", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtra(
                        "NOTIFICATION_ID", notificationId
                    ).putExtra("ALARM_ACTION", "PRE_ALARM"),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
    }

    fun createAlarmNotification(context: Context, msg: String, notificationId: Int): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), AudioManager.STREAM_ALARM
            ).addAction(
                0, "STOP", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtra(
                        "NOTIFICATION_ID", notificationId
                    ).putExtra("ALARM_ACTION", "ON_ALARM"),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
    }

    fun createPostAlarmNotification(
        context: Context, msg: String, notificationId: Int
    ): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true).addAction(
                0, "DISMISS", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtra(
                        "NOTIFICATION_ID", notificationId
                    ).putExtra("ALARM_ACTION", "POST_ALARM"),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        getNotificationManager(context).cancel(notificationId)
    }

    private fun getAlarmNotificationChannel(): NotificationChannel = NotificationChannel(
        CHANNEL_ID_ALARM, CHANNEL_NAME_ALARM, NotificationManager.IMPORTANCE_DEFAULT
    )
}


/**
 * Common Utils
 */
object CommonUtils {
    fun checkCurrentTimePassed(currentTime: LocalDateTime): Boolean {
        return currentTime.isBefore(System.currentTimeMillis().toLocalDateTime())
    }

    fun checkPreAlarmTimePassed(currentTime: LocalDateTime): Boolean {
        return currentTime.isBefore(System.currentTimeMillis().toLocalDateTime().plusMinutes(3))
    }
}


/**
 * Alarm Utils
 */
object AlarmUtils {

    private var ALARM_MANAGER_INSTANCE: AlarmManager? = null

    private fun getAlarmManager(context: Context): AlarmManager =
        ALARM_MANAGER_INSTANCE ?: synchronized(Any()) {
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
                ALARM_MANAGER_INSTANCE = this
            }
        }

    private fun schedulePreAlarm(alarmId: Int, alarm: Alarm, context: Context) {

        val alarmBundle = Bundle().apply {
            putInt("NOTIFICATION_ID", alarmId)
            putString("ALARM_TYPE", "PRE_ALARM")
            putString("EXTRA_MESSAGE", "${alarm.time} Alarm will ring soon")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, PRE), PendingIntent.getBroadcast(
                context, alarmId, Intent(context, AlarmReceiver::class.java).putExtras(
                    alarmBundle
                ), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        Log.d("TAG", "pre alarm schedule: called")

        /*getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, PRE), alarm.hashCode().toString(), {
                val notificationManager = NotificationUtils.getNotificationManager(context)
                notificationManager.notify(
                    alarmId, NotificationUtils.createPreAlarmNotification(
                        context, "${alarm.time} Alarm will ring soon", alarmId
                    )
                )
            }, null
        )*/
    }

    fun schedule(alarmId: Int, alarm: Alarm, context: Context) {

        if (!CommonUtils.checkPreAlarmTimePassed(alarm.time.toLocalDateTime())) {
            schedulePreAlarm(alarmId, alarm, context)
        }
        schedulePostAlarm(alarmId, alarm, context)

        val alarmBundle = Bundle().apply {
            putInt("NOTIFICATION_ID", alarmId)
            putString("ALARM_TYPE", "ON_ALARM")
            putString("EXTRA_MESSAGE", "${alarm.time} Alarm is ringing")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, SCHEDULED), PendingIntent.getBroadcast(
                context, alarmId, Intent(context, AlarmReceiver::class.java).putExtras(
                    alarmBundle
                ), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        Log.d("TAG", "schedule: method called")

        /*getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, SCHEDULED), alarm.hashCode().toString(), {
                val notificationManager = NotificationUtils.getNotificationManager(context)
                notificationManager.notify(
                    alarmId, NotificationUtils.createAlarmNotification(
                        context, "${alarm.time} Alarm is ringing", alarmId
                    )
                )
            }, null
        )*/
    }

    private fun schedulePostAlarm(alarmId: Int, alarm: Alarm, context: Context) {

        val alarmBundle = Bundle().apply {
            putInt("NOTIFICATION_ID", alarmId)
            putString("ALARM_TYPE", "POST_ALARM")
            putString("EXTRA_MESSAGE", "${alarm.time} Alarm rang few minutes ago")
        }

        getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, POST), PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmReceiver::class.java).putExtras(alarmBundle),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.d("TAG", "post alarm schedule: called")

        /*getAlarmManager(context).setExact(
            AlarmManager.RTC_WAKEUP, getAlarmTime(alarm, POST), alarm.hashCode().toString(), {
                val notificationManager = NotificationUtils.getNotificationManager(context)
                notificationManager.notify(
                    alarmId, NotificationUtils.createPostAlarmNotification(
                        context, "${alarm.time} Alarm rang few minutes ago", alarmId
                    )
                )
            }, null
        )*/
    }

    fun cancel(alarm: Alarm, context: Context) {
        getAlarmManager(context).cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                Intent(context, AlarmDismissReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }


    private fun getAlarmTime(alarm: Alarm, timeType: AlarmTimeType): Long = when (timeType) {
        PRE -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L) - (3 * 60 * 1000)
        }

        SCHEDULED -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L)
        }

        POST -> {
            (alarm.time.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000L) + (3 * 60 * 1000)
        }
    }
}