package com.sdu.moneyapp

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessagingService as GoogleFirebaseMessagingService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class NotificationsManager : GoogleFirebaseMessagingService() {

    fun getToken() {
//        FirebaseMessaging.getInstance().token
//            .addOnSuccessListener (OnSuccessListener { task ->
//                // Get new FCM registration token
//                val token = task.result
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
//                // TODO: toast
//            })
//            .addOnFailureListener(OnFailureListener { task ->
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                // TODO?: toast
//            })
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
