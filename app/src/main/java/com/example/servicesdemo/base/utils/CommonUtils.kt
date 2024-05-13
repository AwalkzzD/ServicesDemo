package com.example.servicesdemo.base.utils

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.servicesdemo.R
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_ID
import com.example.servicesdemo.base.utils.AppConstants.CHANNEL_NAME

object DialogUtils {

    fun getProgressDialog(context: Context): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(R.layout.progress_dialog)
        dialogBuilder.setCancelable(false)
        return dialogBuilder.create()
            .apply { window?.setBackgroundDrawableResource(android.R.color.transparent) }
    }

    fun showDialog(
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

object NotificationUtils {

    fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun createNotification(context: Context, count: Int): Notification {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

        getNotificationManager(context).createNotificationChannel(channel)

        return NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Counter Service")
            .setContentText("Counter: $count").setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true).build()
    }
}