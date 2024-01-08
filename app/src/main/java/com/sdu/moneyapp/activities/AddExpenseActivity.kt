package com.sdu.moneyapp.activities

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
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sdu.moneyapp.MessagingService
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.*

class AddExpenseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = { ExpenseScreen() }
        )
    }

    private fun loadGroups(list: MutableList<Group>) {
        list.clear()
        GroupDatabase.getGroupsByUser(AuthManager.getCurrentUserUid()) { groups ->
            list.add(groups)
        }
    }

    private fun loadParticipantsForGroup(groupId: String, list: MutableList<User>) {
        list.clear()
        GroupDatabase.getGroupById(groupId) { group ->
            val users = group.participants
            for (it in users) {
                UserDatabase.getUserById(it) { user ->
                    list.add(user)
                }
            }
        }
    }

    private fun onAddExpenseClick(
        group: Group,
        amount: String,
        description: String,
        participants: List<String>
    ) {
        if (amount.isBlank()) {
            Toast.makeText(
                this@AddExpenseActivity,
                "Please enter an amount",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (participants.isEmpty()) participants.plus(group.participants)
        ExpenseDatabase.createExpense(
            amount.toDouble(),
            description,
            AuthManager.getCurrentUserUid(),
            group.uid,
            participants
        )

        // TODO: Notify participants
        MessagingService.sendNotification()

        finish()
    }

    @Preview
    @Composable
    fun ExpenseScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = { finish() },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "Back")
            }
            var amount by remember { mutableStateOf("") }
            TextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = "Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            var description by remember { mutableStateOf("") }
            TextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = { Text(text = "Description") }
            )

            val participantOptions = remember { mutableStateListOf<User>() }
            val participants = remember { mutableStateListOf<String>() }
            var expandedParticipant by remember { mutableStateOf(false) }

            var selectedGroup by remember { mutableStateOf(false) }
            var groupOb by remember {
                mutableStateOf(
                    Group(
                        "1",
                        "Test Group",
                        "Test Description",
                        listOf()
                    )
                )
            }
            var expanded by remember { mutableStateOf(false) }
            val groupOptions = remember { mutableStateListOf<Group>() }

            // Group Loader
            loadGroups(groupOptions)

            Box {
                TextButton(onClick = { expanded = true; expandedParticipant = false }) {
                    Text(text = if (!selectedGroup) "Choose Group" else groupOb.name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    groupOptions.forEach { groupObDrop ->
                        DropdownMenuItem(
                            text = { Text(text = groupObDrop.name) },
                            onClick = {
                                groupOb = groupObDrop
                                expanded = false
                                selectedGroup = true
                                loadParticipantsForGroup(groupOb.uid, participantOptions) // Load participants
                                participants.clear()
                                expandedParticipant = true
                            }
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expandedParticipant,
                onDismissRequest = { expandedParticipant = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                participantOptions.forEach { participant ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = participants.contains(participant.uid),
                                    onCheckedChange = null // Checkbox does not change programmatically in this case
                                )
                                Text(text = participant.name)
                            }
                        },
                        onClick = {
                            if (participants.contains(participant.uid)) {
                                participants.remove(participant.uid)
                            } else {
                                participants.add(participant.uid)
                            }
                        })
                }

                Button(
                    onClick = { onAddExpenseClick(groupOb, amount, description, participants) },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    enabled = selectedGroup
                ) {
                    Text(text = "Save Expense")
                }
            }
        }

    }
}

