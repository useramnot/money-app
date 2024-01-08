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

    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private val otherUser : String by lazy { intent.getStringExtra("participant") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun onSettleClick()

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
