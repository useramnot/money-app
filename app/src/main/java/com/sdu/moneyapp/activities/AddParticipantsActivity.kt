package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.User


class AddParticipantsActivity : ComponentActivity() {

    private val groupId by lazy { intent.getStringExtra("groupId") }
    private val groupName by lazy { intent.getStringExtra("groupName") }
    private val groupDescription by lazy { intent.getStringExtra("groupDescription") ?: " "}
    private val groupParticipants = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(
            content = { AddParticipantScreen() }
        )
    }

    private fun onBackClick() = finish()

    private fun onAddParticipantClick(participantEmail: String) : MutableList<String> {
        try {
            UserDatabase.getUserByEmail(participantEmail) {
                if (it != null) {
                    if (!groupParticipants.contains(it.uid))
                        groupParticipants.add(it.uid)
                    else
                        Toast.makeText(this, "User already added", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(this, "No user with this email", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // TODO: catch only specific error
            Toast.makeText(this, "Error adding participant with this email", Toast.LENGTH_SHORT).show()
        }
        return groupParticipants.toMutableList()
    }

    private fun onDoneClick() {
        if (groupId == null) {
            GroupDatabase.createGroup(groupName!!, groupDescription, groupParticipants.plus(AuthManager.getCurrentUserUid())) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        else if (groupId != null) {
            GroupDatabase.getGroupById(groupId!!) { group ->
                for (user in groupParticipants){
                    group.addParticipant(user)
                }
                GroupDatabase.setGroup(group)
            }
            finish()
        }
    }

    @Composable
    fun AddParticipantScreen() {
        var participantEmail by remember { mutableStateOf("") }
        val participants = remember { mutableStateListOf<String>() }
        //var participants by remember { mutableStateOf(mutableListOf<String>()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                ) { Text(text = stringResource(id = R.string.back_button)) }
                Button(
                    onClick = { onDoneClick() },
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.End)
                ) { Text(text = "Done") }
            }

            // Participant Email EditText
            TextField(
                value = participantEmail,
                onValueChange = { participantEmail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = stringResource(id = R.string.enter_participant_s_email)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )

            // Add Participant Button
            Button(
                onClick = {
                    participants.clear()
                    participants.addAll(onAddParticipantClick(participantEmail))
              },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) { Text(text = stringResource(id = R.string.add_participant)) }

            // List of participants
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(participants) { participantUid ->
                    Text(text = participantUid)
                }
            }
        }
    }
}

