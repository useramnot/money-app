package com.sdu.moneyapp.databases

import com.sdu.moneyapp.model.Balance

object BalanceDatabase : DatabaseManager() {

    fun createBalance(balance: Balance): Balance {
        balance.uid = getBalancesCollection().document().id
        setBalance(balance)
        return balance
    }
    
    private fun getBalanceByUsersInvolved(groupId: String, user1: String, user2: String, callback: (Balance) -> Unit) {
        val u1: String = if (user1 < user2) user1 else user2
        val u2: String = if (user1 < user2) user2 else user1
        getBalancesCollection().whereEqualTo("user1", u1)
                               .whereEqualTo("user2", u2)
                               .whereEqualTo("groupId", groupId)
                               .get().addOnSuccessListener {
            if (it.size() == 0) {
                callback(createBalance(Balance("", 0.0, u1, u2)))
            } else {
                callback(it.elementAt(0).toObject(Balance::class.java))
            }
        }
    }

    fun user1OwesUser2Amount(groupId: String, user1: String, user2: String, amount: Double) {
        //If amount is positive, user1 owes user2
        //If amount is negative, user2 owes user1
        var switch = false
        if (user1 == user2) return
        if (user1 > user2) switch = true
        val u1 = if (switch) user2 else user1
        val u2 = if (switch) user1 else user2
        getBalanceByUsersInvolved(groupId, u1, u2) { balance ->
            balance.amount += amount * if (switch) -1 else 1
            setBalance(balance)
        }
    }

    fun getOverAllOwing(userId: String, callback: (Double) -> Unit) {
        getBalancesCollection().whereEqualTo("user1", userId).get().addOnSuccessListener { it1 ->
            var sum = 0.0
            for (balance in it1) {
                sum += balance.toObject(Balance::class.java).amount
            }
            getBalancesCollection().whereEqualTo("user2", userId).get().addOnSuccessListener { it2 ->
                for (balance in it2) {
                    sum -= balance.toObject(Balance::class.java).amount
                }
                callback(sum)
            }
        }
    }

    fun getOwingOnGroupWithUser(groupId: String, userId: String, otherUser: String, callback: (Double) -> Unit) {
        var switch = false
        if (userId > otherUser) switch = true
        getBalanceByUsersInvolved(groupId, userId, otherUser) { balance ->
            callback(balance.amount * if (switch) 1 else -1)
        }
    }

    fun getOwingsOnGroup(groupId: String, userId: String) {

    }

    fun settleAmountWithUser(groupId: String, userId: String, otherUser: String, amount: Double, callback: (Double) -> Unit) {
        var switch = false
        if (userId > otherUser) switch = true
        getBalanceByUsersInvolved(groupId, userId, otherUser) { balance ->
            balance.amount = -amount * if (switch) 1 else -1
            setBalance(balance)
            callback(balance.amount)
        }
    }

    fun setBalance(balance: Balance) {
        getBalancesCollection().document(balance.uid).set(balance)
    }
}