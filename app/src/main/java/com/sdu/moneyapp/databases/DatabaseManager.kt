package com.sdu.moneyapp.databases

import com.google.firebase.firestore.*

open class DatabaseManager {
    final val database = FirebaseFirestore.getInstance()

    fun getUsersCollection() : CollectionReference{
        return database.collection("users")
    }

    fun getGroupsCollection() : CollectionReference {
        return database.collection("groups")
    }

    fun getExpensesCollection() : CollectionReference {
        return database.collection("expenses")
    }
}