package com.sdu.moneyapp.classes

import java.util.Date

class Expense (
    val description : String,
    val amount : Double,
    val date : Date,
    val payee : User,
    var payers : List<User>)
{

}