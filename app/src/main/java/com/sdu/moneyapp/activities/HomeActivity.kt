package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.*

class HomeActivity : ComponentActivity() {
    private lateinit var groups:  List<Group>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadGroups()

        setContent(
            content = { HomeScreen() }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadGroups()
    }

    private fun onLogoClick() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun onAddExpenseClick() {
        startActivity(Intent(this, AddExpenseActivity::class.java))
    }

    private fun onCreateGroupClick(){
        startActivityForResult(Intent(this, GroupCreationActivity::class.java), 0)
    }

    private fun onGroupClick(group : Group){
        val intent = Intent(this, GroupOverviewActivity::class.java)
        intent.putExtra("groupId", group.uid)
        startActivityForResult(intent, 1)
    }

    private fun loadGroups() {
        val list = ArrayList<Group>()
        GroupDatabase.getGroupsByUser(AuthManager.getCurrentUserUid()) {
            list.add(it)
            Log.d("MYAPP", "Group " + it.name + " added to options")
        }
        groups = list
    }

    @Preview
    @Composable
    fun HomeScreen(groupViewModel: GroupsViewModel = GroupsViewModel()) {

        val groups by remember { groupViewModel.groups }
        // val groupsList by remember { mutableStateOf(groups) }
        // loadGroups(groups)

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo (Image)
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onLogoClick() }
                )

                // App Name (Text)
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // Overall Owing Text
            Text(
                text = stringResource(id = R.string.overall_owing_placeholder),
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            // TODO: Search Groups

            // Create Group Button
            Button(
                onClick = { onCreateGroupClick() },
                modifier = Modifier.padding(vertical = 8.dp)
            ) { Text(text = "Create") }

            // List of Groups
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(groupsList) { group ->
                    Log.d("MYAPP", "Displaying: $group")
                    Button (
                        onClick = { onGroupClick(group) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ){
                        Column {
                            Text(
                                text = group.name,
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text (
                                text = group.groupDescription,
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Add Expense Button
            Button(
                onClick = { onAddExpenseClick() },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.add_expense))
            }
        }
    }
}

// A View model for updating groups in the UI
class GroupsViewModel : ViewModel() {
    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    fun loadGroups() {
        val list = ArrayList<Group>()
        GroupDatabase.getGroupsByUser(AuthManager.getCurrentUserUid()) {
            list.add(it)
            Log.d("MYAPP", "Group " + it.name + " added to options")
        }
        _groups.value = list
    }
}
