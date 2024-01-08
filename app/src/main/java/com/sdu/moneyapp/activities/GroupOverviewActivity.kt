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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*

class GroupOverviewActivity : ComponentActivity() {

    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = { GroupDetailsScreen() }
        )
    }

    private fun onSettleUpClick(groupId: String) {
        val intent = Intent(this, SettleUpActivity::class.java)
        intent.putExtra("groupId", groupId)
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
        var groupName by remember { mutableStateOf("Group Name") }
        var groupDescription by remember { mutableStateOf("Group Description") }
        val groupExpense = remember { mutableStateListOf<String>()}

        GroupDatabase.getGroupById(groupId) { group ->
            groupName = group.name
            groupDescription = group.groupDescription
            groupExpense.clear()
            for (expense in group.expenses) {
                groupExpense.add(expense)
            }
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
                onClick = { onSettleUpClick(groupId) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.settle_up_button))
            }

            // List of Expenses
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(groupExpense) { expenseId ->
                    var amount by remember { mutableDoubleStateOf(0.0) }
                    var description by remember { mutableStateOf("Expense Description") }
                    var payer by remember { mutableStateOf("Payer") }
                    var involved by remember { mutableStateOf(false)}

                    ExpenseDatabase.getExpenseById(expenseId) { it ->
                        amount = it.amount
                        description = it.description
                        if (it.payerUid == AuthManager.getCurrentUserUid()) {
                            payer = "You"
                        }
                        else {
                            UserDatabase.getUserById(it.payerUid) { user ->
                                payer = user.name
                            }
                        }
                        UserDatabase.getUserById(it.payerUid) { user ->
                            payer = user.name
                        }
                        if ((it.participants.contains(AuthManager.getCurrentUserUid()) || it.payerUid == AuthManager.getCurrentUserUid())) {
                            involved = true
                        }
                    }
                    if (involved){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.White)
                        ) {
                            Text(
                                text = "$payer spent $amount on $description",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
