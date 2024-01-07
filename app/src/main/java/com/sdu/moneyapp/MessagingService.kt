package com.sdu.moneyapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService as GoogleFirebaseMessagingService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class NotificationsManager : GoogleFirebaseMessagingService() {

    fun getToken() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener  { result ->
                // Get new FCM registration token
                val token = result
                Log.d(TAG, "User's FCM registration token: $token")
            }
            .addOnFailureListener {
                Log.w(TAG, "Fetching FCM registration token failed " + it.message)
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO:

        // Handle FCM messages here
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if the message contains data
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if the message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    fun sendNotification() {
        // TODO
    }

    companion object {
        private const val TAG = "MoneyAppFirebaseMsgService"
    }
}
