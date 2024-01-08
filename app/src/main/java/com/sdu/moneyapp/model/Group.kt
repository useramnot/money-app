package com.sdu.moneyapp.model

import com.sdu.moneyapp.databases.*

data class Group(
    val uid: String, // Unique identifier for the group
    var name: String,
    var groupDescription: String,
    var participants: MutableList<String>, // List of UIDs of users in the group
    var expenses: MutableList<String> // List of expenses associated with the group
) {
    constructor(id: String, name: String, desc: String, participants: Set<String>) :
            this(id, name, desc, participants.toMutableList(), mutableListOf())
    constructor(id: String, name: String, desc: String) :
            this(id, name, desc, mutableListOf(AuthManager.getCurrentUserUid()), mutableListOf())
    constructor() : this("", "", "", mutableListOf(), mutableListOf())  //no-argument constructor

    fun addParticipant(uid: String) {
        if (!participants.contains(uid)) participants.add(uid)
    }

    fun removeParticipant(uid: String){
        if (participants.contains(uid))
            participants.remove(uid)
    }

    fun addExpense(expenseId: String) {
        if (!expenses.contains(expenseId)) {
            expenses.add(expenseId)
        }
    }
}

