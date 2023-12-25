package com.sdu.moneyapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class AddParticipantsActivity : AppCompatActivity() {

    private lateinit var editTextParticipantEmail: EditText
    private lateinit var buttonAddParticipant: Button

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_participant)

        editTextParticipantEmail = findViewById(R.id.editTextParticipantEmail)
        buttonAddParticipant = findViewById(R.id.buttonAddParticipant)

        buttonAddParticipant.setOnClickListener {
            addParticipantToGroup()
        }
    }

    private fun addParticipantToGroup() {
        // TODO: Implement logic to add participant to the group
        // Get the email input from editTextParticipantEmail
        val participantEmail = editTextParticipantEmail.text.toString().trim()

        // Check if the user with the provided email exists
        // You may want to query the database to check for the existence of the user
        // If the user exists, add them to the group; otherwise, show an error message
    }
}
