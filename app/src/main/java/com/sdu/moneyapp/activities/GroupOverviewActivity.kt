package com.sdu.moneyapp.activities

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.sdu.moneyapp.ExpenseAdapter
import com.sdu.moneyapp.FirebaseDatabaseManager
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.Expense
import com.sdu.moneyapp.model.Group

class GroupOverviewActivity : AppCompatActivity() {

    private lateinit var textViewGroupName: TextView
    private lateinit var textViewGroupDescription: TextView
    private lateinit var textViewMySummary: TextView
    private lateinit var buttonSettleUp: Button
    private lateinit var listViewExpenses: ListView

    private val databaseManager: FirebaseDatabaseManager by lazy { FirebaseDatabaseManager }

    private lateinit var currentUserUid: String

    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private lateinit var group: Group


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GroupDatabase.getGroupById(groupId) {
            group = it
        }

        setContent(
            content = { GroupDetailsScreen() }
        )
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

    private fun onSettleUpClick() {
        val intent = Intent(this, SettleUpActivity::class.java)
        intent.putExtra("groupId", intent.getStringExtra("groupId"))
        startActivity(intent)
    }

    private fun onGroupSettingsClick(groupId: String) {
        val intent = Intent(this, GroupSettingsActivity::class.java)
        intent.putExtra("groupId", groupId)
        startActivity(intent)
    }

    @Preview
    @Composable
    fun GroupDetailsScreen() {
        var groupName by remember { mutableStateOf(group.name) }
        var groupDescription by remember { mutableStateOf(group.groupDescription) }
        val groupExpense = remember { mutableStateListOf<String>()}

        GroupDatabase.getGroupById(groupId) { group ->
            groupName = group.name
            groupDescription = group.groupDescription
            groupExpense.addAll(group.expenses)
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Back Button
            Button(
                onClick = { finish() },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }

            Button(
                onClick = { onGroupSettingsClick(groupId) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.group_settings))
            }

            // Group Details
            Text(
                text = groupName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = groupDescription,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Settle Up Button
            Button(
                onClick = { onSettleUpClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.settle_up_button))
            }

            // List of Expenses
            LazyColumn(
            ) {
                items(groupExpense) { expenseId ->
                    var amount by remember { mutableDoubleStateOf(0.0) }
                    var description by remember { mutableStateOf("Expense Description") }
                    var involved by remember { mutableStateOf(false)}

                    ExpenseDatabase.getExpenseById(expenseId) { it ->
                        amount = it.amount
                        description = it.description
                        if ((it.participants.contains(AuthManager.getCurrentUserUid()) || it.payerUid == AuthManager.getCurrentUserUid())) {
                            involved = true
                        }
                    }
                    if (involved){
                        Column (
                            //set visibility to gone if the user is not involved in the expense
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)

                        )
                        {
                            Text(
                                text = amount.toString(),
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = description,
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
