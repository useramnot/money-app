package com.sdu.moneyapp.classes

import android.media.Image

class Group
    // A group has users, a name, a profile picture, and a list of expenses
    (var users: List<User>,
     var name: String,
     var profilePicture: Image)
{
    var debts: MutableMap<User, MutableMap<User, Double>> = users.associateWith { users.associateWith { 0.0 }.toMutableMap()}.toMutableMap()

    //var debts is the matrix of debts between users
    var expenses: List<Expense> = emptyList()
    fun addExpense(expense: Expense) {
        expenses += expense
        addDebtsFromExpense(expense)
    }
    fun addDebtsFromExpense(expense: Expense) {
        //will go through the expense and calculate the debts of each user, it will then update the map of debts
        for (payer in expense.payers) {
            if (!debts.containsKey(payer))
                debts[payer] = mutableMapOf()
            if (!debts[payer]!!.contains(expense.payee))
                debts[payer]!![expense.payee] = 0.0
            debts[payer]!![expense.payee]?.plus(expense.amount / expense.payers.size)
            //if the total goes from negative to positive, remove the debt

        }
    }
    fun getDebts(user: User): Map<User, Double> {
        //will return the debts of a user
        return debts[user]!!
    }
    fun addUser(user: User) {
        users += user
        debts[user] = mutableMapOf()
    }
    fun settleUp(payer: User, payee: User, amount: Double) {
        //will settle up the debt between two users
        debts[payer]!![payee]!!.minus(amount)
        //if the total is negative, it means that the payer owes the payee money
    }
}