package com.sdu.moneyapp.activities


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdu.moneyapp.R

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
        // Get the email input from editTextParticipantEmail
        val participantEmail = editTextParticipantEmail.text.toString().trim()

        // Check if the user with the provided email exists
        // You may want to query the database to check for the existence of the user
        // If the user exists, get their UID and add it to the group participants
        val usersReference = database.reference.child("users")
        usersReference.orderByChild("email").equalTo(participantEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val participantUid = userSnapshot.key ?: ""
                            addUserToGroup(participantUid)
                        }
                    } else {
                        // User with the provided email does not exist
                        Toast.makeText(
                            this@AddParticipantsActivity,
                            "User not found with the provided email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    Toast.makeText(
                        this@AddParticipantsActivity,
                        "Error checking user existence",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addUserToGroup(participantUid: String) {
        // Add participant UID to the list of participants in the group
        val participantsReference = database.reference.child("groupParticipants").child(groupId)
        participantsReference.child(participantUid).setValue(true)

        // Display a success message
        Toast.makeText(
            this@AddParticipantsActivity,
            "Participant added to the group.",
            Toast.LENGTH_SHORT
        ).show()

        // Clear the input field after adding the participant
        editTextParticipantEmail.text.clear()
    }
}

