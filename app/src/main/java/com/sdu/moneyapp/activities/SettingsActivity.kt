package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.AuthManager
import com.sdu.moneyapp.databases.UserDatabase

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = { SettingsScreen() }
        )
    }

    private fun onChangeProfileClick() {
        startActivity(Intent(this, ChangeProfileActivity::class.java))
    }

    private fun onNotificationSettingsClick() {
        startActivity(Intent(this, NotificationSettingsActivity::class.java))
    }

    private fun onSignOutClick() {
        AuthManager.signOutUser()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    @Preview
    @Composable
    fun SettingsScreen() {
        var username by remember { mutableStateOf("Username") }
        UserDatabase.getUserById(AuthManager.getCurrentUserUid()) { user ->
            username = user.name
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Back Button
            Button(
                onClick = { finish() },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }

            // Profile Picture and User Name
            Text(
                text = username.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Change Profile Option
            Button(
                onClick = { onChangeProfileClick() },
                modifier = Modifier.padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.change_profile))
            }

            // Notification Settings Option
            Button(
                onClick = { onNotificationSettingsClick() },
                modifier = Modifier.padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.notification_settings))
            }

            // Sign Out Button
            Button(
                onClick = { onSignOutClick() },
                modifier = Modifier.padding(top = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.sign_out))
            }
        }
    }
}
