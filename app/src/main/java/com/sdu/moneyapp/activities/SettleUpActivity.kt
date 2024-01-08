package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdu.moneyapp.MessagingService
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.*



class SettleUpActivity : ComponentActivity() {
    private val currentUserUid = AuthManager.getCurrentUserUid()
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = { SettleUpScreen() }
        )
    }

    private fun onSettleUpClick(participant: String) {
        val intent = Intent(this, SettleUpDetailsActivity::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("participant", participant)
        startActivity(intent)
    }

    private fun onBackClick() {
        finish()
    }

    @Preview
    @Composable
    fun SettleUpScreen() {
        // Sample data, replace with actual data
        var groupName by remember { mutableStateOf("GroupName") }
        var groupDescription by remember { mutableStateOf("Group Description") }
        val participantList = remember { mutableStateListOf<String>() }
        GroupDatabase.getGroupById(groupId) {
            groupName = it.name
            participantList.addAll(it.participants)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Back Button
            Button(
                onClick = { onBackClick() },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
            ) { Text(text = "Back") }

            // Group Details
            Text(
                text = "Your debts with other users in $groupName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            // List of Debts
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(participantList) { participant ->
                    if (participant != AuthManager.getCurrentUserUid()) {
                        var amount by remember { mutableDoubleStateOf(0.0) }
                        var name by remember { mutableStateOf("Another user") }
                        UserDatabase.getUserById(participant) {
                            name = it.name
                        }
                        BalanceDatabase.getOwingOnGroupWithUser(
                            groupId,
                            currentUserUid,
                            participant
                        ) {
                            amount = it
                        }
                        if (amount != 0.0) {
                            Row {
                                Text(
                                    text = if (amount > 0) "You owe $name $amount" else "$name owes you $amount",
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                if (amount > 0)
                                    Button(
                                        onClick = {
                                            onSettleUpClick(participant)
                                        },
                                        modifier = Modifier
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                            .padding(top = 16.dp)
                                    ) { Text(text = "Settle Up") }
                            }
                        }
                    }
                }
            }
        }
    }
}
