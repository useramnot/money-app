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


    // Function to add/update owed amounts for an expense
    fun updateOwedAmounts(expenseId: String, owedAmounts: Map<String, Double>) {
        expensesRef.child(expenseId).child("owedAmounts").setValue(owedAmounts)
    }

/*
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
    }*/

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
}
