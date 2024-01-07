package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.sdu.moneyapp.activities.HomeActivity
import com.sdu.moneyapp.activities.LoginActivity
import com.sdu.moneyapp.databases.AuthManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        // Check if user is logged in, and redirect to HomeActivity if so, if not, LoginActivity
        if (!AuthManager.isUserSignedIn())
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}