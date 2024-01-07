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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.sdu.moneyapp.NotificationsManager

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*

class GroupCreationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = { CreateGroupScreen() }
        )
//        setContentView(R.layout.activity_group_creation)
//
//        editTextGroupName = findViewById(R.id.editTextGroupName)
//        editTextGroupDescription = findViewById(R.id.editTextGroupDescription)
//        buttonCreateGroup = findViewById(R.id.buttonCreateGroup)
//
//
//        buttonCreateGroup.setOnClickListener {
//            val groupName = editTextGroupName.text.toString().trim()
//            val groupDescription = editTextGroupDescription.text.toString().trim()
//
//            if (groupName.isEmpty()) {
//                editTextGroupName.error = "Group Name is required."
//                return@setOnClickListener
//            }
//            /*
//            // Create a new group in the database
//            val groupReference = database.reference.child("groups").push()
//            val groupId = groupReference.key ?: ""
//
//            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//            val participants = listOf(currentUserUid)
//
//            val group = Group(groupId, groupName, groupDescription, participants)
//            groupReference.setValue(group)
//            */
//
//            GroupDatabase.createGroup(groupName, groupDescription, listOf(AuthManager.getCurrentUserUid()));
//
//            finish()
    }

    private fun onBackClick() = finish()

    private fun onCreateGroupClick() {}

    @Composable
    fun CreateGroupScreen() {
        var groupName by remember { mutableStateOf("") }
        var groupDescription by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Back Button
            Button(
                onClick = { /* TODO: Handle back button click */ },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }

            // Group Name EditText
            TextField(
                value = groupName,
                onValueChange = { groupName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text(text = stringResource(id = R.string.group_name)) },
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
                label = { Text(text = stringResource(id = R.string.group_description)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

            // Create Group Button
            Button(
                onClick = { /* TODO: Handle create group button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.create_group))
            }
        }
    }
}
