package com.sdu.moneyapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        backButton = findViewById(R.id.buttonBack)
        emailEditText = findViewById(R.id.editTextEmail)
        resetPasswordButton = findViewById(R.id.buttonResetPassword)

        backButton.setOnClickListener { finish() }

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isNotEmpty()) {
                // Send a password reset email
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Password reset email sent successfully
                            Toast.makeText(
                                this, "Password reset email sent. Check your inbox.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Handle password reset failure
                            Toast.makeText(
                                this, "Failed to send password reset email. Check your email address.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Email field is empty
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
