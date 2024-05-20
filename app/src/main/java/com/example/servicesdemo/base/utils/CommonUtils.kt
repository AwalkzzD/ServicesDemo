package com.example.servicesdemo.base.utils

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
import androidx.core.app.NotificationCompat
import com.example.servicesdemo.R
import com.example.servicesdemo.base.extensions.toLocalDateTime
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_ALARM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_JS
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID_WM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_ALARM
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_JS
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME_WM
import com.example.servicesdemo.ui.home.MainActivity
import com.example.servicesdemo.ui.home.alarm.utils.AlarmDismissReceiver
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


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
        alarmId: Int, notificationId: Int, message: String, context: Context
    ): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true).addAction(
                0, "CANCEL ALARM", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtras(
                        Bundle().apply {
                            putInt("ALARM_ID", alarmId)
                            putInt("NOTIFICATION_ID", notificationId)
                            putString("ALARM_ACTION", "PRE_ALARM")
                        }),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
    }

    fun createAlarmNotification(
        alarmId: Int, notificationId: Int, message: String, context: Context
    ): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), AudioManager.STREAM_ALARM
            ).addAction(
                0, "STOP", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtras(
                        Bundle().apply {
                            putInt("ALARM_ID", alarmId)
                            putInt("NOTIFICATION_ID", notificationId)
                            putString("ALARM_ACTION", "ON_ALARM")
                        }),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
    }

    fun createPostAlarmNotification(
        alarmId: Int, notificationId: Int, message: String, context: Context
    ): Notification {
        getNotificationManager(context).createNotificationChannel(getAlarmNotificationChannel())

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM).setContentTitle("Alarm")
            .setPriority(NotificationManager.IMPORTANCE_MAX).setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setAutoCancel(true).addAction(
                0, "DISMISS", PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmDismissReceiver::class.java).putExtras(
                        Bundle().apply {
                            putInt("ALARM_ID", alarmId)
                            putInt("NOTIFICATION_ID", notificationId)
                            putString("ALARM_ACTION", "POST_ALARM")
                        }),
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

    fun checkPreAlarmTimePassed(alarmTime: LocalDateTime): Boolean {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), alarmTime) >= 2
    }
}