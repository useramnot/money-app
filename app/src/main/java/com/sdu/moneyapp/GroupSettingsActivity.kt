package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GroupSettingsActivity : AppCompatActivity() {

    private lateinit var editTextNewGroupName: EditText
    private lateinit var editTextNewGroupDescription: EditText
    private lateinit var buttonUpdateGroupInfo: Button
    private lateinit var buttonAddParticipant: Button
    private lateinit var listViewParticipants: ListView
    private lateinit var buttonLeaveGroup: Button

    private lateinit var participantsAdapter: ParticipantsAdapter

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_settings)

        editTextNewGroupName = findViewById(R.id.editTextNewGroupName)
        editTextNewGroupDescription = findViewById(R.id.editTextNewGroupDescription)
        buttonUpdateGroupInfo = findViewById(R.id.buttonUpdateGroupInfo)
        buttonAddParticipant = findViewById(R.id.buttonAddParticipant)
        listViewParticipants = findViewById(R.id.listViewParticipants)
        buttonLeaveGroup = findViewById(R.id.buttonLeaveGroup)

        // Set up the ParticipantsAdapter with the onRemoveParticipantClick callback
        participantsAdapter = ParticipantsAdapter(this, R.layout.item_participant) { participantUid ->
            // TODO: Implement logic to remove the participant from the group
            // You may show a confirmation dialog before removing the participant
            removeParticipantFromGroup(participantUid)
        }

        // Set up the ListView for participants
        listViewParticipants.adapter = participantsAdapter

        // Load and display participants
        loadParticipants()

        buttonUpdateGroupInfo.setOnClickListener {
            updateGroupInfo()
        }

        buttonAddParticipant.setOnClickListener {
            // TODO: Implement logic to add participants
            // This could involve navigating to a screen or showing a dialog for participant addition
        }

        listViewParticipants.setOnItemClickListener { _, _, position, _ ->
            // TODO: Implement logic if needed when a participant is clicked
        }

        buttonLeaveGroup.setOnClickListener {
            // TODO: Implement logic to leave the group
            // This could involve showing a confirmation dialog before leaving the group
        }
    }

    private fun loadParticipants() {
        val participantsReference = database.reference.child("groupParticipants").child(groupId)

        participantsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                participantsAdapter.clear()

                for (childSnapshot in snapshot.children) {
                    val participantUid = childSnapshot.key ?: ""
                    participantsAdapter.add(participantUid)
                }

                // Notify the adapter that the data set has changed
                participantsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@GroupSettingsActivity, "Error loading participants", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateGroupInfo() {
        val newGroupName = editTextNewGroupName.text.toString().trim()
        val newGroupDescription = editTextNewGroupDescription.text.toString().trim()

        if (newGroupName.isEmpty()) {
            editTextNewGroupName.error = "Group Name is required."
            return
        }

        // Update group information in the database
        val groupReference = database.reference.child("groups").child(groupId)
        groupReference.child("name").setValue(newGroupName)
        groupReference.child("groupDescription").setValue(newGroupDescription)

        Toast.makeText(this, "Group information updated", Toast.LENGTH_SHORT).show()
    }

    private fun removeParticipantFromGroup(participantUid: String) {
        // TODO: Implement the logic to remove the participant from the group
        // You may want to show a confirmation dialog before removing the participant
    }
}
