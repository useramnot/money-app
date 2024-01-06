package com.sdu.moneyapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdu.moneyapp.R

class SettleUpDetailsActivity : AppCompatActivity() {

    private lateinit var spinnerPayer: Spinner
    private lateinit var spinnerPayee: Spinner
    private lateinit var editTextAmount: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonBack: Button

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settle_up_details)

        spinnerPayer = findViewById(R.id.spinnerPayer)
        spinnerPayee = findViewById(R.id.spinnerPayee)
        editTextAmount = findViewById(R.id.editTextAmount)
        buttonSave = findViewById(R.id.buttonSave)
        buttonBack = findViewById(R.id.buttonBack)

        // Set up spinners with group participants
        loadGroupParticipants()

        buttonSave.setOnClickListener {
            saveSettleUpDetails()
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun loadGroupParticipants() {
        val participantsReference = database.reference.child("groupParticipants").child(groupId)

        participantsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupParticipants = snapshot.children.mapNotNull { it.key }

                // Update spinners with group participants
                updateSpinnerAdapter(spinnerPayer, groupParticipants)
                updateSpinnerAdapter(spinnerPayee, groupParticipants)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateSpinnerAdapter(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


    private fun saveSettleUpDetails() {
        // TODO: Implement logic to save settle-up details
        // You may want to get the selected payer, payee, and amount
        // and update the database accordingly
        val payer = spinnerPayer.selectedItem.toString()
        val payee = spinnerPayee.selectedItem.toString()
        val amount = editTextAmount.text.toString().toDoubleOrNull() ?: 0.0

        // Update the database with settle-up details
        // ...

        // Close the activity
        finish()
    }
}
