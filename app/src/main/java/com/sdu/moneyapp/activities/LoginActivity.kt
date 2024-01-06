package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.AuthenticationManager

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = {
                LoginScreen()
            }
        )
    }

    private fun onLoginClick(email: String, password: String) {
        AuthenticationManager.signInUser(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this, "Authentication failed. Please check your email and password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun onRegisterClick(email: String, password: String) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun onForgotPasswordClick() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    @Composable
    @Preview
    fun LoginScreen() {
        val context = LocalContext.current

        var isPasswordVisible by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(id = R.string.app_logo),
                modifier = Modifier
                    .fillMaxWidth()
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = context.getString(R.string.email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                placeholder = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (isPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {isPasswordVisible = !isPasswordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onForgotPasswordClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Forgot Password")
            }

            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = { onRegisterClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }
        }
    }
}
