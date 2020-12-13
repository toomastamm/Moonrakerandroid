package com.example.moonraker_android.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.*
import com.example.moonraker_android.R
import com.example.moonraker_android.ui.print_status.PrintStatusAPI
import com.example.moonraker_android.utils.Utils

class PrintWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        const val INPUT_ESTIMATED_TIME = "estimated_time"
        const val PREFS_NOTIFICATION_ENABLED = "notification_enabled"
        const val NOTIFICATION_ID = "moonraker_print_notification"
        const val NOTIFICATION_INT_ID = 4159
        const val TAG = "PrintWorker"
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
    private val notificationEnabled = preferences.getBoolean(PREFS_NOTIFICATION_ENABLED, false)

    override suspend fun doWork(): Result {
        Log.d(TAG, "Started worker")
        if (!notificationEnabled) {
            return Result.success()
        }
        val progress = "Starting print"
        setForeground(createForegroundInfo(progress))
        updatePrintStatus()
        return Result.success()
    }

    private fun updatePrintStatus() {
        while (!this.isStopped) {
            if (!notificationEnabled) {
                break
            }
            val status = PrintStatusAPI.getPrintState()
            if (status.state == "printing") {
                val progress = Utils.secondsToHoursMinutesSeconds(status.print_duration)
                val title = applicationContext.getString(R.string.notification_title)
                val notification = createNotification(title, progress)
                NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_INT_ID, notification)
            } else {
                break
            }
        }
    }

    private fun createNotification(title: String, progress: String): Notification {
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_menu_print_status)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            // .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(progress: String): ForegroundInfo {
        // val cancel = applicationContext.getString(R.string.cancel_download)
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val title = applicationContext.getString(R.string.notification_title)
        val notification = createNotification(title, progress)
        return ForegroundInfo(NOTIFICATION_INT_ID, notification)
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