package com.sdu.moneyapp

// Inside com.sdu.moneyapp package

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
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
            // This could involve showing a confirmation dialog
            // and updating the database, and refreshing the UI
            settleUpOrRemind(participantUid, isOwed)
        }

        // Set up the ListView for settle up participants
        listViewParticipants.adapter = settleUpAdapter

        // Load and display settle up participants
        loadSettleUpParticipants()
    }

    private fun loadSettleUpParticipants() {
        // TODO: Implement logic to load settle up participants
        // You may want to query the database to get the list of unsettled participants in the group
        // Update settleUpAdapter with the list of participants
    }

    private fun settleUpOrRemind(participantUid: String, isOwed: Boolean) {
        // TODO: Implement logic for settling up or reminding based on isOwed
        // This could involve showing a confirmation dialog
        // and updating the database, and refreshing the UI
    }
}
