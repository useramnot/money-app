package com.sdu.moneyapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.User
import kotlin.properties.Delegates

object NotificationManager {
    fun getNewExpenseNotificationSetting(notificationSettings: Int) : Boolean = notificationSettings.toString()[0] == '1'
    fun getExpenseReminderNotificationSetting(notificationSettings: Int) : Boolean = notificationSettings.toString()[1] == '1'
    fun getGroupUpdateNotificationSetting(notificationSettings: Int) : Boolean = notificationSettings.toString()[2] == '1'
    fun getNotificationSettings(callback: (Int) -> Unit) {
        UserDatabase.getUserById(AuthManager.getCurrentUserUid()) { user ->
            callback(user.notificationSettings)
        }
    }


    private fun adjustedNewExpenseNotificationSetting(notificationSettings: Int, setting: Boolean) =
        setting.toString().toInt() * 100 + notificationSettings % 100
    private fun adjustedExpenseReminderNotificationSetting(notificationSettings: Int, setting: Boolean) =
        notificationSettings / 100 + setting.toString().toInt() * 10 + notificationSettings % 10
    private fun adjustedGroupUpdateNotificationSetting(notificationSettings: Int, setting: Boolean) =
        notificationSettings.toString().slice(IntRange(0, 1)).toInt() + setting.toString().toInt()

    fun setNotificationSettings(newExpense: Boolean, expenseReminder: Boolean, groupUpdate: Boolean) {
        UserDatabase.getUserById(AuthManager.getCurrentUserUid()) { user ->
            user.notificationSettings = adjustedNewExpenseNotificationSetting(user.notificationSettings, newExpense)
            user.notificationSettings = adjustedExpenseReminderNotificationSetting(user.notificationSettings, expenseReminder)
            user.notificationSettings = adjustedGroupUpdateNotificationSetting(user.notificationSettings, groupUpdate)
            UserDatabase.setUser(user) { Log.d("MYAPP", "Changin user notificationSettings to ${user.notificationSettings}") }
        }
    }

    fun displayNotification(context: Context, title: String?, body: String?, msgType: Int) {
        // TODO
//        val notificationBuilder = NotificationCompat.Builder(context, "MYAPP_NOTIFICATIONS")
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notificationBuilder.build())
    }



    fun sendNotification() {
        // TODO
    }

}