package com.sdu.moneyapp.model

data class Balance (
    var uid: String,
    var amount: Double,
    val user1: String,
    val user2: String
){
    //no-argument constructor
    constructor() : this("", 0.0, "", "")
    //user1 id < user2 id
    //If amount is positive, user1 owes user2
    //If amount is negative, user2 owes user1
}