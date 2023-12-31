package com.sdu.moneyapp.model

data class User(
    val uid: String, // Unique identifier for the user
    val name: String, // User's display name
    val email: String, // User's email address
    val newExpenseNotification: Boolean = false,
    val expenseReminders: Boolean = false,
    val groupUpdates: Boolean = false
)
