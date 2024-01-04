package com.sdu.moneyapp

// Inside com.sdu.moneyapp package

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SettleUpActivity : AppCompatActivity() {

    private lateinit var listViewParticipants: ListView
    private lateinit var settleUpAdapter: SettleUpAdapter

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settle_up)

        listViewParticipants = findViewById(R.id.listViewSettleUpParticipants)

        settleUpAdapter = SettleUpAdapter(this, R.layout.item_settle_up_participant) { participantUid, isOwed ->
            // TODO: Implement logic for settling up or reminding based on isOwed
            // TODO: Send notification for reminding
            // This could involve showing a confirmation dialog
            // and updating the database, and refreshing the UI
            settleUpOrRemind(participantUid, isOwed)
        }

        listViewParticipants.adapter = settleUpAdapter

        loadSettleUpParticipants()
    }

    private fun loadSettleUpParticipants() {
        val participantsReference = database.reference.child("groupParticipants").child(groupId)

        participantsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unsettledParticipants = mutableListOf<String>()

                for (participantSnapshot in snapshot.children) {
                    val participantUid = participantSnapshot.key
                    // TODO: Implement logic to check if the participant is owed or settled up
                    // For now, let's assume all participants are unsettled
                    if (participantUid != null) {
                        unsettledParticipants.add(participantUid)
                    }
                }

                // Update the adapter with the list of unsettled participants
                settleUpAdapter.clear()
                settleUpAdapter.addAll(unsettledParticipants)
                settleUpAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SettleUpActivity, "Error loading settle up participants", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun settleUpOrRemind(participantUid: String, isOwed: Boolean) {
        // If isOwed is false, navigate to a new activity
        if (!isOwed) {
            // TODO: Implement your navigation logic here
            // For example, you can create a new Intent to start a SettleUpDetailsActivity
            val intent = Intent(this, SettleUpDetailsActivity::class.java)
            intent.putExtra("participantUid", participantUid)
            startActivity(intent)
        } else {
            // TODO: Implement logic for reminding based on isOwed
            // This could involve showing a confirmation dialog
            // and updating the database, and refreshing the UI
            // For now, let's just print a message
        }
    }

}
