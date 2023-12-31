package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var imageViewLogo: ImageView

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseManager: FirebaseDatabaseManager by lazy { FirebaseDatabaseManager }

    private lateinit var groupAdapter: GroupAdapter
    private lateinit var originalGroups: List<Group>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textViewOverallOwing = findViewById(R.id.textViewOverallOwing)
        editTextSearchGroups = findViewById(R.id.editTextSearchGroups)
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup)
        listViewGroups = findViewById(R.id.listViewGroups)
        buttonAddExpense = findViewById(R.id.buttonAddExpense)
        imageViewLogo = findViewById(R.id.imageViewLogo)

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

        buttonAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        // Retrieve and display list of groups
        if (currentUserUid != null) {
            databaseManager.getGroupsForUser(currentUserUid) { groups ->
                originalGroups = groups
                groupAdapter = GroupAdapter(this, originalGroups)
                listViewGroups.adapter = groupAdapter
            }
        }

        editTextSearchGroups.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this implementation
            }

            override fun afterTextChanged(editable: Editable?) {
                val searchText = editable.toString().trim()
                val filteredGroups = originalGroups.filter { group ->
                    group.name.contains(searchText, ignoreCase = true)
                }
                groupAdapter.updateGroups(filteredGroups)
            }
        })

        listViewGroups.setOnItemClickListener { _, _, position, _ ->
            val selectedGroup = listViewGroups.getItemAtPosition(position) as Group

            val intent = Intent(this, GroupOverviewActivity::class.java)

            intent.putExtra("groupId", selectedGroup.id)
            intent.putExtra("groupName", selectedGroup.name)
            intent.putExtra("groupDescription", selectedGroup.groupDescription)

            startActivity(intent)
        }

        imageViewLogo.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

}