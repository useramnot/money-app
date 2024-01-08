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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.sdu.moneyapp.MessagingService
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.*

class GroupSettingsActivity : ComponentActivity() {

    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private var group: Group = Group()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GroupDatabase.getGroupById(groupId) {
            group = it
            setContent(
                content = { GroupSettingsScreen(group) }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resetActivity()
    }

    private fun resetActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun onBackClick() = finish()

    private fun onAddParticipantsClick() {
        val intent = Intent(this, AddParticipantsActivity::class.java)
        intent.putExtra("groupId", groupId)
        startActivityForResult(intent, 0)
    }

    private fun onRemoveParticipantClick(participantUid: String) {
        group.participants.remove(participantUid)
        resetActivity()
    }

    private fun onSaveChangesClick(groupName: String, groupDescription: String) {
        if (groupName != "") group.name = groupName
        if (groupDescription != "") group.groupDescription = groupDescription
        GroupDatabase.setGroup(group)
        finish()
    }

    @Composable
    fun GroupSettingsScreen(group: Group) {
        var groupName by remember { mutableStateOf(group.name) }
        var groupDescription by remember { mutableStateOf(group.groupDescription) }
        var groupParticipants by remember { mutableStateOf(group.participants) }

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
            ) {
                Text(text = "Back")
            }

            // Group Name EditText
            TextField(
                value = groupName,
                onValueChange = { groupName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = "Group name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

            // Group Description EditText
            TextField(
                value = groupDescription,
                onValueChange = { groupDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = "Group description") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

            // Add PArticipants Button
            Button(
                onClick = { onAddParticipantsClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Add Participants")
            }

            // List of Expenses
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(groupParticipants) { participantUid ->
                    Row {
                        Text(text = participantUid)
                        Button(onClick = { onRemoveParticipantClick(participantUid) }) {

                        }
                    }
                }
            }

            // Save Changes Button
            Button(
                onClick = { onSaveChangesClick(groupName, groupDescription) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Save changes")
            }
        }
    }
}
