package com.sdu.moneyapp.model

import com.google.firebase.firestore.DocumentId

data class Group(
    @DocumentId
    val uid: String, // Unique identifier for the group
    val name: String,
    val groupDescription: String,
    val participants: List<String>, // List of UIDs of users in the group
    val expenses: List<Expense> = emptyList() // List of expenses associated with the group
)

