package com.sdu.moneyapp.model

data class Expense(
    val uid: String, // Unique identifier for the expense
    val amount: Double,
    val description: String,
    val payerUid: String, // UID of the user who paid the expense
    val groupId: String, // UID of the group the expense is associated with
    val date: Long, // Date the expense was created
    val participants: List<String>, // List of UIDs of users who participated in the expense
    val owedAmounts: Map<String, Double> = emptyMap() // Map of UIDs of users who participated in the expense to the amount they owe
){
    //no-argument constructor
    constructor() : this("", 0.0, "", "", "", 0, emptyList(), emptyMap())

    fun getOwed() : Double {
        return amount / (participants.size + 1)
    }
}


