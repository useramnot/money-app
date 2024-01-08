package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.sdu.moneyapp.activities.HomeActivity
import com.sdu.moneyapp.activities.LoginActivity
import com.sdu.moneyapp.databases.AuthManager
import com.sdu.moneyapp.MessagingService


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseMessaging: FirebaseMessaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        //firebaseMessaging = FirebaseMessaging.getInstance()
        //firebaseMessaging.isAutoInitEnabled = true

        //MessagingService.subscribeNotifications()

        // Check if user is logged in, and redirect to HomeActivity if so, if not, LoginActivity
        if (!AuthManager.isUserSignedIn())
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}