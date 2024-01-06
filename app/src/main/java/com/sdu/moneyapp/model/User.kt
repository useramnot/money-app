package com.sdu.moneyapp.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val uid: String,                                     // Unique identifier for the user
    val name: String,                                   // User's display name
    var notificationSettings: Int = 111,                // First position ('0' or '1'): new expense reminders,
                                                        // Second position ('0' or '1'): expense reminder,
                                                        // Third position ('0' or '1'): group updates
    val groups: List<String> = emptyList()              // List of group IDs that the user is a member of
){
    //fun getUid(): String = uid

    //fun getName() : String = name

    //fun getGroups() : List<String> = groups
    fun addGroup(groupId: String) = groups.plus(groupId)
    fun removeGroup(groupId: String) = groups.minus(groupId)

    fun getNotificationSettings() : Number = notificationSettings

    fun getNewExpenseNotificationSetting() : Boolean = notificationSettings.toString()[0] == '1'
    fun getExpenseReminderNotificationSetting() : Boolean = notificationSettings.toString()[1] == '1'
    fun getGroupUpdateNotificationSetting() : Boolean = notificationSettings.toString()[2] == '1'

    fun setNewExpenseNotificationSetting(setting: Boolean) {
        notificationSettings = setting.toString().toInt() * 100 + notificationSettings % 100
    }
    fun setExpenseReminderNotificationSetting(setting: Boolean) {
        notificationSettings = notificationSettings / 100 + setting.toString().toInt() * 10 + notificationSettings % 10
    }
    fun setGroupUpdateNotificationSetting(setting: Boolean) {
        notificationSettings = notificationSettings.toString().slice(IntRange(0, 1)).toInt() + setting.toString().toInt()
    }
}
