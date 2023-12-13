package com.sdu.moneyapp.classes

import android.media.Image

class Group
    // A group has users, a name, a profile picture, and a list of expenses
    (var users: List<User>,
     var name: String,
     var profilePicture: Image)
{
    var debts: Map<User, Map<User, Double>> = users.map({ user -> user to users.map({ user2 -> user2 to 0.0 }).toMap() }).toMap()
    //var debts is the matrix of debts between users
    var expenses: List<Expense> = emptyList()
    fun addExpense(expense: Expense) {
        expenses += expense
        calculateDebts(expense)
    }
    fun calculateDebts(expense: Expense) {
        //will go through the expense and calculate the debts of each user, it will then update the map of debts

    }
    fun getDebts(user: User): Map<User, Double> {
        //will return the list of debts of a user
        return debts.get(user)!!
    }
    fun addUser(user: User) {
        users += user
        debts += user to users.map({ user2 -> user2 to 0.0 }).toMap()
    }
}