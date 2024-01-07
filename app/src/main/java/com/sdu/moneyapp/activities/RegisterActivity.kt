package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.UserDatabase


class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = { RegisterForm() }
        )
    }

    private fun onRegisterClick(name: String, email: String, reEmail: String, password: String, rePassword: String) {
        if (email != reEmail) {
            Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show()
        }
        else if (password != rePassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            UserDatabase.createUser(name, email, password, {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            },
            { msg : String ->
                Toast.makeText(this, "Registration failed. $msg", Toast.LENGTH_SHORT).show()
            })
        }
    }

    @Preview
    @Composable
    fun RegisterForm() {
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isRepeatPasswordVisible by remember { mutableStateOf(false) }
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var repeatEmail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    // Handle back button click
                    // For example, finish the activity
                    (context as? RegisterActivity)?.finish()
                },
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(id = R.string.name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = repeatEmail,
                onValueChange = { repeatEmail = it },
                label = { Text(text = stringResource(id = R.string.repeat_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            /*Text(
                text = stringResource(id = R.string.emails_do_not_match),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .visibility(if (email != repeatEmail) Visibility.Visible else Visibility.Collapsed)
            )*/

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text(text = stringResource(id = R.string.repeat_password)) },
                singleLine = true,
                visualTransformation = if (isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { isRepeatPasswordVisible = !isRepeatPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isRepeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            /*Text(
                text = "Password does not match",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .visibility(if (password != repeatPassword) Visibility.Visible else Visibility.Collapsed
            )*/

            Button(
                onClick = { onRegisterClick(name, email, repeatEmail, password, repeatPassword) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

