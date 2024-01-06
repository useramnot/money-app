package com.sdu.moneyapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sdu.moneyapp.R
import com.sdu.moneyapp.model.Expense

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var editTextAmount: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerPayer: Spinner
    private lateinit var multiAutoCompleteTextViewParticipants: MultiAutoCompleteTextView
    private lateinit var buttonSaveExpense: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private val currentUserUid: String by lazy { auth.currentUser?.uid ?: "" }

    private lateinit var groupParticipants: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDescription = findViewById(R.id.editTextDescription)
        spinnerPayer = findViewById(R.id.spinnerPayer)
        multiAutoCompleteTextViewParticipants = findViewById(R.id.multiAutoCompleteTextViewParticipants)
        buttonSaveExpense = findViewById(R.id.buttonSaveExpense)

        loadGroupParticipants()

        populatePayerSpinner()

        configureParticipantsMultiAutoCompleteTextView()

        buttonSaveExpense.setOnClickListener {
            saveExpense()
        }
    }


    private fun loadGroupParticipants() {
        val participantsReference = databaseReference.child("groupParticipants").child(groupId)

        participantsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupParticipants = snapshot.children.mapNotNull { it.key }
                populatePayerSpinner()
                configureParticipantsMultiAutoCompleteTextView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddExpenseActivity, "Error loading group participants", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun populatePayerSpinner() {
        val payerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupParticipants)
        payerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPayer.adapter = payerAdapter
    }

    private fun configureParticipantsMultiAutoCompleteTextView() {
        val participantsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, groupParticipants)
        multiAutoCompleteTextViewParticipants.setAdapter(participantsAdapter)
        multiAutoCompleteTextViewParticipants.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }

    private fun saveExpense() {
        val amount = editTextAmount.text.toString().toDoubleOrNull() ?: return
        val description = editTextDescription.text.toString().trim()
        val payerUid = groupParticipants.getOrNull(spinnerPayer.selectedItemPosition) ?: currentUserUid

        val participantsInput = multiAutoCompleteTextViewParticipants.text.toString()
        val participants = if (participantsInput.isNotEmpty()) {
            participantsInput.split(",").map { it.trim() }
        } else {
            groupParticipants
        }

        // Calculate owed amounts (divide the amount equally among participants)
        val numberOfParticipants = participants.size
        val owedAmountPerParticipant = amount / numberOfParticipants

        // Create a map of owed amounts for each participant
        val owedAmounts = participants.associateWith { owedAmountPerParticipant }

        // Save the expense to the database
        val expenseId = databaseReference.child("expenses").push().key ?: ""
        val expense = Expense(expenseId, amount, description, payerUid, participants, owedAmounts)

        val expenseReference = databaseReference.child("groups").child(groupId).child("expenses").child(expenseId)
        expenseReference.setValue(expense)

        // TODO: Send notification

        finish()
    }

}
