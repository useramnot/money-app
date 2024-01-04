package com.sdu.moneyapp

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdu.moneyapp.model.Expense
import com.sdu.moneyapp.model.Group

object FirebaseDatabaseManager {

    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private val expensesRef: DatabaseReference by lazy { database.reference.child("expenses") }
    private val groupsRef: DatabaseReference by lazy { database.reference.child("groups") }

    // Function to add an expense to the database
    fun addExpense(expense: Expense) {
        expensesRef.child(expense.id).setValue(expense)
    }

    // Function to add/update owed amounts for an expense
    fun updateOwedAmounts(expenseId: String, owedAmounts: Map<String, Double>) {
        expensesRef.child(expenseId).child("owedAmounts").setValue(owedAmounts)
    }

    fun getExpenses(callback: (List<Expense>) -> Unit) {
        // TODO
        expensesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = snapshot.children.mapNotNull { it.getValue(Expense::class.java) }
                callback(expenses)
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO
                // Handle the error, e.g., display an error message
            }
        })
    }

    fun getOverallOwing(userUid: String, callback: (Double) -> Unit) {
        // TODO
        val expensesRef: DatabaseReference = database.reference.child("expenses")

        expensesRef.orderByChild("participants/$userUid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var overallOwing = 0.0

                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(Expense::class.java)

                        // Calculate overall owing based on expense details
                        // Update this part based on your app's logic
                        if (expense != null) {
                            overallOwing += calculateOwingForExpense(expense, userUid)
                        }
                    }

                    callback(overallOwing)
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO
                    // Handle the error, e.g., display an error message
                }
            })
    }

    // Function to calculate owing amount for a specific expense
    private fun calculateOwingForExpense(expense: Expense, userUid: String): Double {
        // TODO
        // Implement your app's logic to calculate owing amount for an expense
        // This can involve checking the payer and participants, and updating the owed/owing amounts
        // Replace this with your actual logic
        return 0.0
    }

    fun getGroupsForUser(userUid: String, callback: (List<Group>) -> Unit) {
        val groupsRef: DatabaseReference = database.reference.child("groups")

        groupsRef.orderByChild("participants/$userUid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val groups = snapshot.children.mapNotNull { it.getValue(Group::class.java) }
                    callback(groups)
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO
                    // Handle the error, e.g., display an error message
                }
            })
    }

    // Function to get expenses for a user in a group
    fun getExpensesForUserInGroup(userUid: String, groupId: String, callback: (List<Expense>) -> Unit) {
        val expensesRef: DatabaseReference = database.reference.child("expenses")

        expensesRef.orderByChild("groupId").equalTo(groupId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val expenses = snapshot.children
                        .mapNotNull { it.getValue(Expense::class.java) }
                        .filter { userUid in it.participants }

                    callback(expenses)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("handle error")
                }
            })
    }

    fun settleUpForUserInGroup(currentUserUid: String, groupId: String, function: () -> Unit) {
        // TODO: update expenses
    }


    fun getGroupParticipants(groupId: String, callback: (List<String>) -> Unit) {
        groupsRef.child(groupId).child("participants")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val participants = snapshot.children.mapNotNull { it.key }
                    callback(participants)
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO: Handle the error
                }
            })
    }
}
