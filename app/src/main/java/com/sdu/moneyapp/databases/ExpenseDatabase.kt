package com.sdu.moneyapp.databases

import android.util.Log
import com.sdu.moneyapp.model.Expense
import com.sdu.moneyapp.model.User
import java.util.Date

object ExpenseDatabase : DatabaseManager()
{
    private val savedExpenses = HashMap<String, Expense>()
    private val savedExpensesAge = HashMap<String, Long>()

    fun createExpense(amount: Double, description: String, creator: String, groupId: String, participants: List<String>) {
        val id = getExpensesCollection().document().id
        val otherParticipants = participants.minus(creator)
        for (it in otherParticipants) {
            Log.d("MYAPP", "otherPArticipant: $it")
        }
        setExpense(Expense(id, amount, description, creator, groupId, System.currentTimeMillis(), otherParticipants))
        val owed = amount / (otherParticipants.size + 1)
        GroupDatabase.getGroupById(groupId) { group ->
            group.addExpense(id)
            GroupDatabase.setGroup(group)
            for (user in otherParticipants) {
                BalanceDatabase.user1OwesUser2Amount(groupId, user, creator, owed)
            }
        }
    }

    fun getExpenseById(expenseId : String, callback: (Expense) -> Unit ) {
        if (savedExpenses.containsKey(expenseId)) {
            if (System.currentTimeMillis() - savedExpensesAge.getValue(expenseId) > SUITABLE_AGE) {
                savedExpenses.remove(expenseId)
                savedExpensesAge.remove(expenseId)
            }else {
                callback(savedExpenses.getValue(expenseId))
            }
        }
        getExpensesCollection().document(expenseId).get().addOnSuccessListener {
            val expense = it.toObject(Expense::class.java) ?: throw Exception("Expense $expenseId failed to be found")
            savedExpenses[expenseId] = expense
            savedExpensesAge[expenseId] = System.currentTimeMillis()
            callback(expense)
        }
    }

    fun setExpense(data : Expense) {
        getExpensesCollection().document(data.uid).set(data).addOnSuccessListener {
            savedExpenses[data.uid] = data
            savedExpensesAge[data.uid] = System.currentTimeMillis()
        }.addOnFailureListener {
            throw Exception("Expense " + data.uid + " failed to set")
        }
    }

}