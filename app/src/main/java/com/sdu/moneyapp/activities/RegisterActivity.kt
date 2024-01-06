package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.UserDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var emailEditTextRep: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordEditTextRep: EditText
    private lateinit var emailMismatchError: TextView
    private lateinit var passwordMismatchError: TextView
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        backButton = findViewById(R.id.buttonBack)
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        emailEditTextRep = findViewById(R.id.editTextEmailRep)
        passwordEditText = findViewById(R.id.editTextPassword)
        passwordEditTextRep = findViewById(R.id.editTextPasswordRep)
        emailMismatchError = findViewById(R.id.emailMismatchError)
        passwordMismatchError = findViewById(R.id.passwordMismatchError)
        registerButton = findViewById(R.id.buttonRegister)

        backButton.setOnClickListener { finish() }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val repeatEmail = emailEditTextRep.text.toString()
            val password = passwordEditText.text.toString()
            val repeatPassword = passwordEditTextRep.text.toString()

            // TODO: check for empty strings

            // Reset error messages
            emailMismatchError.visibility = View.INVISIBLE
            passwordMismatchError.visibility = View.INVISIBLE

            // Emails or passwords do not match, show error messages
            if (email != repeatEmail) {
                emailMismatchError.visibility = View.VISIBLE
            }
            else if (password != repeatPassword) {
                passwordMismatchError.visibility = View.VISIBLE
            }
            else {
                // Emails and passwords match, proceed with registration
                val suc = UserDatabase.createUser(name, email, password)
                if (suc) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                else {
                    Toast.makeText(this, "Registration failed. Please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

