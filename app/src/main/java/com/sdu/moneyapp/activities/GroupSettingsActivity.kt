package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sdu.moneyapp.ParticipantsAdapter
import com.sdu.moneyapp.R

class GroupSettingsActivity : AppCompatActivity() {

    private lateinit var buttonUpdateGroupInfo: Button
    private lateinit var buttonAddParticipant: Button
    private lateinit var listViewParticipants: ListView
    private lateinit var buttonLeaveGroup: Button
    private lateinit var buttonBack: Button

    private lateinit var participantsAdapter: ParticipantsAdapter

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_settings)

        buttonUpdateGroupInfo = findViewById(R.id.buttonUpdateGroupInfo)
        buttonAddParticipant = findViewById(R.id.buttonAddParticipant)
        listViewParticipants = findViewById(R.id.listViewParticipants)
        buttonLeaveGroup = findViewById(R.id.buttonLeaveGroup)
        buttonBack = findViewById(R.id.buttonBack)

        buttonBack.setOnClickListener {
            finish()
        }

        participantsAdapter = ParticipantsAdapter(this, R.layout.item_participant) { participantUid ->
            removeParticipantFromGroup(participantUid)
        }

        listViewParticipants.adapter = participantsAdapter

        loadParticipants()

        buttonUpdateGroupInfo.setOnClickListener {
            startActivity(Intent(this, EditGroupInfoActivity::class.java).putExtra("groupId", groupId))
        }

        buttonAddParticipant.setOnClickListener {
            startActivity(Intent(this, AddParticipantsActivity::class.java).putExtra("groupId", groupId))
        }

        listViewParticipants.setOnItemClickListener { _, _, _, _ ->
            // no action bc i would rather die
        }

        buttonLeaveGroup.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Leave Group")
                .setMessage("Are you sure you want to leave the group?")
                .setPositiveButton("Leave") { _, _ ->
                    leaveGroup()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun leaveGroup() {
        val participantsReference = database.reference.child("groupParticipants").child(groupId)

        participantsReference.child(currentUserUid).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Left the group successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to leave the group", Toast.LENGTH_SHORT).show()
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

                participantsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GroupSettingsActivity, "Error loading participants", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeParticipantFromGroup(participantUid: String) {
        AlertDialog.Builder(this)
            .setTitle("Remove Participant")
            .setMessage("Are you sure you want to remove this participant from the group?")
            .setPositiveButton("Remove") { _, _ ->
                val participantsReference = database.reference.child("groupParticipants").child(groupId)

                participantsReference.child(participantUid).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Participant removed from the group", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to remove participant from the group", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
