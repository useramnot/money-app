package com.sdu.moneyapp.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.Group
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = { HomeScreen() }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun onLogoClick() {
        startActivityForResult(Intent(this, SettingsActivity::class.java), 0)
    }

    private fun onAddExpenseClick() {
        startActivityForResult(Intent(this, AddExpenseActivity::class.java), 1)
    }

    private fun onCreateGroupClick(){
        startActivityForResult(Intent(this, GroupCreationActivity::class.java), 2)
    }

    private fun onGroupClick(group : Group){
        val intent = Intent(this, GroupOverviewActivity::class.java)
        intent.putExtra("groupId", group.uid)
        startActivity(intent)
    }

    private fun loadGroups(list: MutableList<Group>) {
        list.clear()
        GroupDatabase.getGroupsByUser(AuthManager.getCurrentUserUid()) {
            list.add(it)
            Log.d("MYAPP", "Group " + it.name + " added to options")
        }
    }

    @Preview
    @Composable
    fun HomeScreen() {

        val groups = remember { mutableStateListOf<Group>() }
        loadGroups(groups)

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
            ) {
                Text(text = stringResource(id = R.string.create_group))
            }

            // List of Groups
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(groups) { group ->
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