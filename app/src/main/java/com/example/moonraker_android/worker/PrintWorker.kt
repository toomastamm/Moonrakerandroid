package com.example.moonraker_android.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.moonraker_android.R
import com.example.moonraker_android.ui.print_status.PrintStatusAPI
import com.example.moonraker_android.utils.Utils

class PrintWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        const val PREFS_NOTIFICATION_ENABLED = "notification_enabled"
        const val NOTIFICATION_ID = "moonraker_print_notification"
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val notificationEnabled = preferences.getBoolean(PREFS_NOTIFICATION_ENABLED, false)
        if (!notificationEnabled) {
            return Result.success()
        }
        val progress = "Starting print"
        setForeground(createForegroundInfo(progress))
        updatePrintStatus()
        return Result.success()
    }

    private suspend fun updatePrintStatus() {
        // Calls setForegroundInfo() periodically when it needs to update
        // the ongoing Notification
        while (!this.isStopped) {
            val status = PrintStatusAPI.getPrintState()
            if (status.state == "printing") {
                val progress = Utils.secondsToHoursMinutesSeconds(status.print_duration)
                setForeground(createForegroundInfo(progress))
            } else {
                break
            }
        }
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = NOTIFICATION_ID
        val title = applicationContext.getString(R.string.notification_title)
        // val cancel = applicationContext.getString(R.string.cancel_download)
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_menu_print_status)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            // .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(4129, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create the NotificationChannel
        val name = applicationContext.getString(R.string.channel_name)
        val descriptionText = applicationContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(NOTIFICATION_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(mChannel)
    }
}