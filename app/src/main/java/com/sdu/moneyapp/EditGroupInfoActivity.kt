package com.sdu.moneyapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditGroupInfoActivity : AppCompatActivity() {

    private lateinit var editTextGroupName: EditText
    private lateinit var editTextGroupDescription: EditText
    private lateinit var buttonSaveChanges: Button

    private val database = FirebaseDatabase.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group_info)

        editTextGroupName = findViewById(R.id.editTextGroupName)
        editTextGroupDescription = findViewById(R.id.editTextGroupDescription)
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges)

        // Set up the back button
        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        buttonSaveChanges.setOnClickListener {
            saveChanges()
        }

        loadGroupDetails()
    }

    private fun loadGroupDetails() {
        val groupReference = database.reference.child("groups").child(groupId)

        groupReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupName = snapshot.child("name").value?.toString() ?: ""
                val groupDescription = snapshot.child("groupDescription").value?.toString() ?: ""

                editTextGroupName.setText(groupName)
                editTextGroupDescription.setText(groupDescription)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@EditGroupInfoActivity,
                    "Error loading group details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveChanges() {
        val newGroupName = editTextGroupName.text.toString().trim()
        val newGroupDescription = editTextGroupDescription.text.toString().trim()

        if (newGroupName.isEmpty()) {
            editTextGroupName.error = "Group Name is required."
            return
        }

        val groupReference = database.reference.child("groups").child(groupId)
        groupReference.child("name").setValue(newGroupName)
        groupReference.child("groupDescription").setValue(newGroupDescription)

        Toast.makeText(this, "Group information updated", Toast.LENGTH_SHORT).show()

        finish()
    }
}
