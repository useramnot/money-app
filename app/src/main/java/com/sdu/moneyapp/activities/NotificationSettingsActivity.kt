package com.sdu.moneyapp.activities

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.AuthManager

class NotificationSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        /*

        buttonBack.setOnClickListener {
            finish()
        }

        loadNotificationSettings()

        switchNewExpenseNotification.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationSetting("newExpenseNotification", isChecked)
        }

        switchExpenseReminders.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationSetting("expenseReminders", isChecked)
        }

        switchGroupUpdates.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationSetting("groupUpdates", isChecked)
        }*/
    }
/*
    private fun loadNotificationSettings() {
        val currentUserUid = auth.currentUser?.uid

        if (currentUserUid != null) {
            val settingsReference = databaseReference.child("notificationSettings").child(currentUserUid)

            settingsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newExpenseNotification = snapshot.child("newExpenseNotification").getValue(Boolean::class.java) ?: false
                    val expenseReminders = snapshot.child("expenseReminders").getValue(Boolean::class.java) ?: false
                    val groupUpdates = snapshot.child("groupUpdates").getValue(Boolean::class.java) ?: false

                    switchNewExpenseNotification.isChecked = newExpenseNotification
                    switchExpenseReminders.isChecked = expenseReminders
                    switchGroupUpdates.isChecked = groupUpdates
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@NotificationSettingsActivity,
                        "Error loading notification settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun saveNotificationSetting(settingKey: String, isEnabled: Boolean) {
        val currentUserUid = AuthManager.getCurrentUserUid()
        val settingsReference = databaseReference.child("notificationSettings").child(currentUserUid)
        settingsReference.child(settingKey).setValue(isEnabled)
    }*/

    @Preview
    @Composable
    fun SettingsScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = { finish() },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }
            SettingItem(
                label = stringResource(id = R.string.new_expense_notification),
                checked = remember { mutableStateOf(false) }
            )

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)

            SettingItem(
                label = stringResource(id = R.string.expense_reminders),
                checked = remember { mutableStateOf(false) }
            )

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)

            SettingItem(
                label = stringResource(id = R.string.group_updates),
                checked = remember { mutableStateOf(false) }
            )

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)
        }
    }


    @Composable
    fun SettingItem(label: String, checked: MutableState<Boolean>) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label)

            Switch(
                checked = checked.value,
                onCheckedChange = { checked.value = it },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }

}
