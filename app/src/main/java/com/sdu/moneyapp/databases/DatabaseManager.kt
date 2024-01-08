package com.sdu.moneyapp.databases

import com.google.firebase.firestore.*

open class DatabaseManager {
    val database = FirebaseFirestore.getInstance()
    val SUITABLE_AGE = 10000 // 10 seconds

    fun getUsersCollection() : CollectionReference{
        return database.collection("users")
    }

    fun getGroupsCollection() : CollectionReference {
        return database.collection("groups")
    }

    fun getExpensesCollection() : CollectionReference {
        return database.collection("expenses")
    }

    fun getBalancesCollection() : CollectionReference {
        return database.collection("balances")
    }
}