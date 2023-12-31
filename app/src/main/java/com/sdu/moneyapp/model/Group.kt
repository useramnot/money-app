package com.sdu.moneyapp.model

data class Group(
    val id: String, // Unique identifier for the group
    val name: String,
    val groupDescription: String,
    val participants: List<String>, // List of UIDs of users in the group
    val expenses: List<Expense> = emptyList() // List of expenses associated with the group
)

