package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sdu.moneyapp.model.Expense

class GroupOverviewActivity : AppCompatActivity() {

    private lateinit var textViewGroupName: TextView
    private lateinit var textViewGroupDescription: TextView
    private lateinit var textViewMySummary: TextView
    private lateinit var buttonSettleUp: Button
    private lateinit var listViewExpenses: ListView

    private val databaseManager: FirebaseDatabaseManager by lazy { FirebaseDatabaseManager }
    private lateinit var currentUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_overview)

        textViewGroupName = findViewById(R.id.textViewGroupName)
        textViewGroupDescription = findViewById(R.id.textViewGroupDescription)
        textViewMySummary = findViewById(R.id.textViewMySummary)
        buttonSettleUp = findViewById(R.id.buttonSettleUp)
        listViewExpenses = findViewById(R.id.listViewExpenses)

        // Retrieve group details from the intent
        val groupId = intent.getStringExtra("groupId") ?: ""
        val groupName = intent.getStringExtra("groupName") ?: "Group Name"
        val groupDescription = intent.getStringExtra("groupDescription") ?: "Group Description"

        textViewGroupName.text = groupName
        textViewGroupDescription.text = groupDescription

        // Fetch and display group summary
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        fetchAndDisplayExpenses(groupId)

        // Settle Up Button Click Listener
        buttonSettleUp.setOnClickListener {
            // TODO
            // Implement the logic for settling up
            // This could involve updating the database and refreshing the UI
        }

        val buttonGroupSettings: Button = findViewById(R.id.buttonGroupSettings)
        buttonGroupSettings.setOnClickListener {
            val intent = Intent(this, GroupSettingsActivity::class.java)
            intent.putExtra("groupId", groupId)
            intent.putExtra("groupName", groupName)
            intent.putExtra("groupDescription", groupDescription)
            startActivity(intent)
        }
    }


    private fun fetchAndDisplayExpenses(groupId: String) {
        databaseManager.getExpensesForUserInGroup(currentUserUid, groupId) { expenses ->
            val expenseAdapter = ExpenseAdapter(this, expenses)
            listViewExpenses.adapter = expenseAdapter

            // Calculate and display group summary based on user's expenses
            val groupSummary = calculateGroupSummary(expenses)
            textViewMySummary.text = groupSummary
        }
    }

    private fun calculateGroupSummary(expenses: List<Expense>): String {
        val totalOwed = expenses.filter { it.payerUid == currentUserUid }.sumOf { it.amount }
        val totalOwing =
            expenses.filter { currentUserUid in it.participants && it.payerUid != currentUserUid }
                .sumOf { it.amount }
        // TODO: it should show only one option, i.e. if you are owed or you owe or you are settled up
        return "You Owe: $$totalOwing\nYou Are Owed: $$totalOwed\nYou Are Settled Up: ${totalOwed - totalOwing}"
    }

    // Back Button Click Listener
    fun onBackButtonClick(view: View) {
        finish()
    }

    // Settle Up Button Click Listener
    fun onSettleUpButtonClick(view: View) {
        // TODO
        // Implement the logic for settling up
        // This could involve updating the database and refreshing the UI
    }

}
