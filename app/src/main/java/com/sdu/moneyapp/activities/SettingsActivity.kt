package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sdu.moneyapp.R
import com.sdu.moneyapp.model.User

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonBack: Button
    private lateinit var textViewUserName: TextView
    private lateinit var buttonChangeProfile: Button
    private lateinit var buttonNotificationSettings: Button
    private lateinit var buttonSignOut: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonBack = findViewById(R.id.buttonBack)
        textViewUserName = findViewById(R.id.textViewUserName)
        buttonChangeProfile = findViewById(R.id.buttonChangeProfile)
        buttonNotificationSettings = findViewById(R.id.buttonNotificationSettings)
        buttonSignOut = findViewById(R.id.buttonSignOut)

        // Retrieve and set the user name
        val currentUserUid = auth.currentUser?.uid
        currentUserUid?.let { uid ->
            val userReference = databaseReference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            val username = it.name
                            textViewUserName.text = username ?: "Username"
                        } ?: run {
                            textViewUserName.setText(R.string.default_username)
                        }
                    } else {
                        textViewUserName.setText(R.string.default_username)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    textViewUserName.setText(R.string.default_username)
                }
            })
        }


        buttonBack.setOnClickListener {
            finish()
        }

        buttonChangeProfile.setOnClickListener {
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }

        buttonNotificationSettings.setOnClickListener {
            startActivity(Intent(this, NotificationSettingsActivity::class.java))
        }

        buttonSignOut.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Sign Out")
            alertDialogBuilder.setMessage("Are you sure you want to sign out?")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}
