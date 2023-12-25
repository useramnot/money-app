package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sdu.moneyapp.model.Group

class HomeActivity : AppCompatActivity() {

    private lateinit var textViewOverallOwing: TextView
    private lateinit var editTextSearchGroups: EditText
    private lateinit var buttonCreateGroup: Button
    private lateinit var listViewGroups: ListView
    private lateinit var buttonAddExpense: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseManager: FirebaseDatabaseManager by lazy { FirebaseDatabaseManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textViewOverallOwing = findViewById(R.id.textViewOverallOwing)
        editTextSearchGroups = findViewById(R.id.editTextSearchGroups)
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup)
        listViewGroups = findViewById(R.id.listViewGroups)
        buttonAddExpense = findViewById(R.id.buttonAddExpense)

        // Example: Display overall owing/owed information
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            databaseManager.getOverallOwing(currentUserUid) { owingAmount ->
                val formattedText = getString(R.string.overall_owing_placeholder, owingAmount)
                textViewOverallOwing.text = formattedText
            }
        }

        buttonCreateGroup.setOnClickListener {
            startActivity(Intent(this, GroupCreationActivity::class.java))
        }

        // Example: Handle click on "Add Expense" button
        buttonAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        // Example: Retrieve and display list of groups

        if (currentUserUid != null) {
            databaseManager.getGroupsForUser(currentUserUid) { groups ->
                // Update your UI with the list of groups
                val groupAdapter = GroupAdapter(this, groups)
                listViewGroups.adapter = groupAdapter
            }
        }

        // Example: Handle click on a group in the list
        listViewGroups.setOnItemClickListener { _, _, position, _ ->
            val selectedGroup = listViewGroups.getItemAtPosition(position) as Group
            // TODO
            // You can navigate to the group overview or details screen
            // Pass the selectedGroup information to the next activity if needed
            // Example: startActivity(Intent(this, GroupOverviewActivity::class.java).putExtra("group", selectedGroup))
        }
    }
}