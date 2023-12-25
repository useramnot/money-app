package com.sdu.moneyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sdu.moneyapp.model.Group

class GroupCreationActivity : AppCompatActivity() {

    private lateinit var editTextGroupName: EditText
    private lateinit var editTextGroupDescription: EditText
    private lateinit var buttonAddParticipants: Button
    private lateinit var buttonCreateGroup: Button

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)

        editTextGroupName = findViewById(R.id.editTextGroupName)
        editTextGroupDescription = findViewById(R.id.editTextGroupDescription)
        buttonAddParticipants = findViewById(R.id.buttonAddParticipants)
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup)

        buttonAddParticipants.setOnClickListener {
            // TODO
            // Implement logic to navigate to participant addition screen or dialog
            // You can use an Intent to start a new activity or show a dialog fragment
        }

        buttonCreateGroup.setOnClickListener {
            createGroup()
        }
    }

    fun onAddParticipantsClick(view: View) {
        // Implement logic to navigate to participant addition screen or dialog
        // You can use an Intent to start a new activity or show a dialog fragment
    }

    private fun createGroup() {
        val groupName = editTextGroupName.text.toString().trim()
        val groupDescription = editTextGroupDescription.text.toString().trim()

        if (groupName.isEmpty()) {
            editTextGroupName.error = "Group Name is required."
            return
        }

        // Create a new group in the database
        val groupReference = database.reference.child("groups").push()
        val groupId = groupReference.key ?: ""

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val participants = listOf(currentUserUid)

        val group = Group(groupId, groupName, groupDescription, participants)
        groupReference.setValue(group)

        navigateToAddParticipantsScreen(groupId)

        // Uncomment the line below if you want to finish the activity after creating the group
        finish()
    }

    private fun navigateToAddParticipantsScreen(groupId: String) {
        val intent = Intent(this, AddParticipantsActivity::class.java)
        intent.putExtra("groupId", groupId)
        startActivity(intent)
    }


}
