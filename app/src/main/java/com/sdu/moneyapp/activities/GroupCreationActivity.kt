package com.sdu.moneyapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.AuthenticationManager
import com.sdu.moneyapp.databases.GroupDatabase
import com.sdu.moneyapp.model.Group

class GroupCreationActivity : AppCompatActivity() {

    private lateinit var editTextGroupName: EditText
    private lateinit var editTextGroupDescription: EditText
    private lateinit var buttonCreateGroup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)

        editTextGroupName = findViewById(R.id.editTextGroupName)
        editTextGroupDescription = findViewById(R.id.editTextGroupDescription)
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup)


        buttonCreateGroup.setOnClickListener {
            val groupName = editTextGroupName.text.toString().trim()
            val groupDescription = editTextGroupDescription.text.toString().trim()

            if (groupName.isEmpty()) {
                editTextGroupName.error = "Group Name is required."
                return@setOnClickListener
            }
            /*
            // Create a new group in the database
            val groupReference = database.reference.child("groups").push()
            val groupId = groupReference.key ?: ""

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val participants = listOf(currentUserUid)

            val group = Group(groupId, groupName, groupDescription, participants)
            groupReference.setValue(group)
            */

            GroupDatabase.createGroup(groupName, groupDescription, listOf(AuthenticationManager.getCurrentUserUid() ?: ""));

            finish()
        }
    }



}
