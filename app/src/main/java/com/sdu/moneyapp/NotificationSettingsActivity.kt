package com.sdu.moneyapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var buttonBack: Button
    private lateinit var switchNewExpenseNotification: SwitchCompat
    private lateinit var switchExpenseReminders: SwitchCompat
    private lateinit var switchGroupUpdates: SwitchCompat

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        buttonBack = findViewById(R.id.buttonBack)
        switchNewExpenseNotification = findViewById(R.id.switchNewExpenseNotification)
        switchExpenseReminders = findViewById(R.id.switchExpenseReminders)
        switchGroupUpdates = findViewById(R.id.switchGroupUpdates)

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
        }
    }

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
        val currentUserUid = auth.currentUser?.uid

        if (currentUserUid != null) {
            val settingsReference = databaseReference.child("notificationSettings").child(currentUserUid)
            settingsReference.child(settingKey).setValue(isEnabled)
        }
    }
}
