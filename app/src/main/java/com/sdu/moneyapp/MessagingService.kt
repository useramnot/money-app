package com.sdu.moneyapp

import android.app.Notification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessagingService as GoogleFirebaseMessagingService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import com.google.firebase.messaging.remoteMessage
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
//import java.net.https

object MessagingService : GoogleFirebaseMessagingService() {

    fun getToken(callback: (String) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener  { result ->
                callback(result)
                Log.d("MONEYAPP_MESSAGING", "User's FCM registration token: $result")
            }
            .addOnFailureListener {
                Log.w("MONEYAPP_MESSAGING", "Fetching FCM registration token failed " + it.message)
            }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MONEYAPP_MESSAGING", "Refreshed token: $token")
        // TODO: Send token to server
    }

    override fun onMessageReceived(messageObj: RemoteMessage) {
        super.onMessageReceived(messageObj)
        val message = messageObj.data;
        Log.d("MONEYAPP_MESSAGING", "Message data: $message")
        NotificationManager.displayNotification(this, message["title"], message["message"], 0)
        // TODO: Display push notification
    }

    fun subscribeNotifications() {
        fun subscribeTopic(topic: String) {
            Firebase.messaging.subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    var msg = "Subscribed to $topic"
                    if (!task.isSuccessful) {
                        msg = "Subscribtion to $topic failed"
                    }
                    Log.d("MONEYAPP_MESSAGING", msg)
                }
        }

        fun unsubscribeTopic(topic: String) {
            Firebase.messaging.unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    var msg = "Unubscribed to $topic"
                    if (!task.isSuccessful) {
                        msg = "Unubscribtion to $topic failed"
                    }
                    Log.d("MONEYAPP_MESSAGING", msg)
                }
        }

        NotificationManager.getNotificationSettings {
            if (it / 100 == 1) subscribeTopic("1")
            else unsubscribeTopic("1")
            if (it / 10 == 1) subscribeTopic("2")
            else unsubscribeTopic("2")
            if (it / 2 == 1) subscribeTopic("3")
            else unsubscribeTopic("3")
        }
    }

    fun sendNotification() {
        fun createNotification(title: String, body: String): JSONObject {
            val notification = JSONObject()
            notification.put("title", true)
            notification.put("body", body)
            return notification
        }

        fun createMessage(notification: JSONObject, topic: String) : JSONObject {
            val message = JSONObject()
            message.put("notification", notification)
            message.put("target", topic)
            return message
        }


        val url = URL("https://fcm.googleapis.com/v1/projects/money-app-sdu/messages:send")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Authorization", "AIzaSyBW2X3bJtOjtTjiak-H6pWYAgVOJxWuR-E")
            setRequestProperty("Content-Type", "application/json; UTF-8")

            val payload = JSONObject()
            payload.put("message", createMessage(createNotification("TEST_TILE", "TEST_BODY"), "1"))
            payload.put("validate_only", false)

            val outputBytes = payload.toString().toByteArray(Charsets.UTF_8)
            doOutput = false
        }
    }
}
