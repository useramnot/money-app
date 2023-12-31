package com.sdu.moneyapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sdu.moneyapp.MainActivity
import com.sdu.moneyapp.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // TODO(napraw to kurwa)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Extract data from the message
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        // Show a notification
        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "expense_notification_channel"
        val notificationId = 1

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the device is running Android Oreo or higher and create a notification channel
        val channel = NotificationChannel(
            channelId,
            "Expense Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
