package com.sdu.moneyapp.model

import com.sdu.moneyapp.databases.*

data class Group(
    val uid: String, // Unique identifier for the group
    var name: String,
    var groupDescription: String,
    var participants: List<String>, // List of UIDs of users in the group
    var expenses: List<String> // List of expenses associated with the group
) {
    constructor(id: String, name: String, desc: String, participants: List<String>) :
            this(id, name, desc, participants, emptyList())
    constructor(id: String, name: String, desc: String) :
            this(id, name, desc, listOf(AuthManager.getCurrentUserUid()), emptyList())
    //no-argument constructor
    constructor() : this("", "", "", emptyList(), emptyList())
    fun addParticipant(uid: String) {
        if (!participants.contains(uid)) {
            participants.plus(uid)
        }
    }

    fun addExpense(expenseId: String) {
        if (!expenses.contains(expenseId)) {
            expenses.plus(expenseId)
        }
    }
}

