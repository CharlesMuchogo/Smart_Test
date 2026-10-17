package com.charlesmuchogo.research

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.charlesmuchogo.research.navigation.DeepLinks
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification ?: return
        val link = remoteMessage.data[DeepLinks.LINK_EXTRA]
        val conversationId = remoteMessage.data[DeepLinks.CONVERSATION_ID_EXTRA]
        val key = link ?: conversationId ?: notification.title.orEmpty()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            link?.let { putExtra(DeepLinks.LINK_EXTRA, it) }
            conversationId?.let { putExtra(DeepLinks.CONVERSATION_ID_EXTRA, it) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this, key.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(notification.title ?: getString(R.string.app_name))
            .setContentText(notification.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        createNotificationChannel()

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(key.hashCode(), notificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "SmartTest Channel"
        val descriptionText = "Very important notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
