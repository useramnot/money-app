package com.sdu.moneyapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.sdu.moneyapp.R

class ChangeProfileActivity : AppCompatActivity() {

    private lateinit var editTextNewName: EditText
    private lateinit var editTextNewEmail: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var buttonSaveChanges: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        editTextNewName = findViewById(R.id.editTextNewName)
        editTextNewEmail = findViewById(R.id.editTextNewEmail)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges)

        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        buttonSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val newName = editTextNewName.text.toString().trim()
        val newEmail = editTextNewEmail.text.toString().trim()
        val newPassword = editTextNewPassword.text.toString().trim()

        val user = auth.currentUser

        if (user != null) {
            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Display name updated successfully
                        Toast.makeText(this, "Display name updated", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle error updating display name
                        Toast.makeText(this, "Error updating display name", Toast.LENGTH_SHORT).show()
                    }
                }

            // Update email
            if (newEmail.isNotEmpty() && newEmail != user.email) {
                user.verifyBeforeUpdateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error updating email", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            // Update password
            if (newPassword.isNotEmpty()) {
                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Password updated successfully
                            Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle error updating password
                            Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}
