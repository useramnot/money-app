package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sdu.moneyapp.ExpenseAdapter
import com.sdu.moneyapp.FirebaseDatabaseManager
import com.sdu.moneyapp.R
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
            FirebaseDatabaseManager.settleUpForUserInGroup(currentUserUid, groupId) {
                fetchAndDisplayExpenses(groupId)
            }
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
        FirebaseDatabaseManager.getExpensesForUserInGroup(currentUserUid, groupId) { expenses ->
            val expenseAdapter = ExpenseAdapter(this, expenses)
            listViewExpenses.adapter = expenseAdapter

            val groupSummary = calculateGroupSummary(expenses)
            textViewMySummary.text = groupSummary
        }
    }

    private fun calculateGroupSummary(expenses: List<Expense>): String {
        val totalOwed = expenses
            .filter { currentUserUid in it.owedAmounts }
            .sumOf { it.owedAmounts[currentUserUid] ?: 0.0 }

        val totalOwing = expenses
            .filter { it.payerUid == currentUserUid }
            .sumOf { it.amount } - expenses
            .filter { currentUserUid in it.participants && it.payerUid == currentUserUid }
            .sumOf { it.amount }

        val balance = totalOwed - totalOwing

        return when {
            balance > 0 -> "You Are Owed: $$balance"
            balance < 0 -> "You Owe: $${-balance}"
            else -> "You Are Settled Up"
        }
    }


    fun onBackButtonClick(view: View) {
        finish()
    }


}
