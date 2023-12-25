package com.sdu.moneyapp.model

data class Expense(
    val id: String, // Unique identifier for the expense
    val amount: Double,
    val description: String,
    val payerUid: String, // UID of the user who paid the expense
    val participants: List<String> // List of UIDs of users who participated in the expense
)
