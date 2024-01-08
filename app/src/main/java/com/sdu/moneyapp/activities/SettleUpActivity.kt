package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.*


class SettleUpActivity : ComponentActivity() {
    private val currentUserUid = AuthManager.getCurrentUserUid()
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GroupDatabase.getGroupById(groupId) {
            group = it
        }

        setContent(
            content = { GroupOverviewScreen() }
        )
    }

    private fun onSettleUpClick(participant: String) {
        val intent = Intent(this, SettleUpDetailsActivity::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("participant", participant)
        startActivity(intent)
    }

    @Preview
    @Composable
    fun GroupOverviewScreen() {
        // Sample data, replace with actual data
        val groupName by remember { mutableStateOf(group.name) }
        val groupDescription by remember { mutableStateOf(group.groupDescription) }
        val participantList by remember { mutableStateOf(group.participants)}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Back Button
            Button(
                onClick = { /* Handle back button click */ },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
            ) { Text(text = "Back") }

            // Group Settings Button
            Button(
                onClick = { /* Handle group settings button click */ },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
                    .padding(top = 8.dp)
            ) { Text(text = "Group Settins") }

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
            // List of Debts
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                items(participantList) { participant ->
                    if (participant == AuthManager.getCurrentUserUid()) return@items
                    var amount by remember { mutableDoubleStateOf(0.0) }
                    var name by remember { mutableStateOf("Another user") }
                    UserDatabase.getUserById(participant) {
                        name = it.name
                    }
                    BalanceDatabase.getOwingOnGroupWithUser(groupId, currentUserUid, participant){
                        amount = it
                    }
                    Row {
                        Text(
                            text = if (amount > 0) "You owe $name $amount" else "$name owes you $amount",
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = {
                                onSettleUpClick(participant)
                            },
                            modifier = Modifier
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                        ) {  Text(text = "Settle Up") }
                    }
                }
            }
        }
    }
}
