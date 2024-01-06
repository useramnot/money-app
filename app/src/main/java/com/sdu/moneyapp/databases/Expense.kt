package com.sdu.moneyapp.databases

import java.util.Date

class Expense (
    val description : String,
    val amount : Double,
    val date : Date,
    val payee : UserDatabase,
    var payers : List<UserDatabase>)
{

}