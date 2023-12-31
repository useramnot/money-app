package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sdu.moneyapp.model.User

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

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

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

            // Reset error messages
            emailMismatchError.visibility = View.INVISIBLE
            passwordMismatchError.visibility = View.INVISIBLE

            if (email == repeatEmail && password == repeatPassword) {
                // Emails and passwords match, proceed with registration
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val database = FirebaseDatabase.getInstance()
                            val usersReference = database.reference.child("users")

                            val newUser = User(user?.uid ?: "", name, email)
                            usersReference.child(user?.uid ?: "").setValue(newUser)
                                .addOnSuccessListener {
                                    // User information stored successfully
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            // Registration successful, navigate to the home screen or another screen
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            // Handle registration failure
                            Toast.makeText(
                                this, "Registration failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Emails or passwords do not match, show error messages
                if (email != repeatEmail) {
                    emailMismatchError.visibility = View.VISIBLE
                }
                if (password != repeatPassword) {
                    passwordMismatchError.visibility = View.VISIBLE
                }
            }
        }
    }
}

