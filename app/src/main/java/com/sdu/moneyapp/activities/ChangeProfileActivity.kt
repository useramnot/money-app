package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*

class ChangeProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = { ChangeProfileScreen() }
        )
    }

    private fun onBackClick() = finish()

    private fun onSaveChangesClick(newName: String, newPassword: String) {
        UserDatabase.getUserById(AuthManager.getCurrentUserUid()) {
            // TODO: more sophisticated validation?
            Log.d("MYAPP", "Changing user settings for " + it.name)
            if (newName.isNotEmpty()) {
                it.name = newName
                Log.d("MYAPP", "Change username to $newName")
            }
            if (newPassword.isNotEmpty()) {
                AuthManager.getCurrentUser()?.updatePassword(newPassword)
                it.name = newName
                Log.d("MYAPP", "Changed password")
            }
            UserDatabase.setUser(it)
            finish()
        }
    }

    @Preview
    @Composable
    fun ChangeProfileScreen() {
        var newName by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Back Button
            Button(
                onClick = { onBackClick() },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }

            // New Name EditText
            TextField(
                value = newName,
                onValueChange = { newName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = stringResource(id = R.string.new_name)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

            // New Password EditText
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = stringResource(id = R.string.new_password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            // Save Changes Button
            Button(
                onClick = { onSaveChangesClick(newName, newPassword) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.save_changes))
            }
        }
    }
}
