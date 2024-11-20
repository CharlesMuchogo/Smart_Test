package com.charlesmuchogo.research

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        sendNotification(
            title = "Your test is ready!",
            message = "The test result is ready. You can now read and upload the results",
            context = context
        )
        return Result.success()
    }

    @SuppressLint("NewApi")
    private fun sendNotification(
        title: String,
        message: String,
        context: Context,
    ) {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("SOURCE", "Local Notifications")
            }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
            )
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(
                "test_channel",
                "Test notifications channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Channel for test notifications"
            }

        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(applicationContext, "test_channel")
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(1, notification)
    }
}