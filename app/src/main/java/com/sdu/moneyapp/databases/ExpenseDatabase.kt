package com.sdu.moneyapp.databases

import com.sdu.moneyapp.model.Expense
import com.sdu.moneyapp.model.User
import java.util.Date

object ExpenseDatabase : DatabaseManager()
{
    private val savedExpenses = HashMap<String, Expense>()
    private val savedExpensesAge = HashMap<String, Long>()

    fun createExpense(amount: Double, description: String, creator: String, groupId: String, participants: List<String>) {
        val id = getExpensesCollection().document().id
        setExpense(Expense(id, amount, description, creator, groupId, System.currentTimeMillis(), participants))
    }

    fun getExpenseById(expenseId : String) : Expense {
        if (savedExpenses.containsKey(expenseId)) {
            if (System.currentTimeMillis() - savedExpensesAge.getValue(expenseId) > SUITABLE_AGE) {
                savedExpenses.remove(expenseId)
                savedExpensesAge.remove(expenseId)
                return getExpenseById(expenseId)
            }
            return savedExpenses.getValue(expenseId)
        }
        val expense = getExpensesCollection().document(expenseId).get().result.toObject(Expense::class.java)
            ?: throw Exception("Expense $expenseId failed to be found")
        savedExpenses[expenseId] = expense
        savedExpensesAge[expenseId] = System.currentTimeMillis()
        return expense
    }

    fun setExpense(data : Expense) {
        getExpensesCollection().document(data.uid).set(data).addOnSuccessListener {
            savedExpenses[data.uid] = data
        }.addOnFailureListener {
            throw Exception("Expense " + data.uid + " failed to set")
        }
        savedExpenses[data.uid] = data
    }

    fun getExpensesByGroupId(groupId : String, callback: (List<Expense>) -> Unit) {
        GroupDatabase.getGroupById(groupId){ group ->
            callback(group.expenses.map { getExpenseById(it)})
        }
    }

}