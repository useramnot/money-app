package com.sdu.moneyapp.databases

import com.google.firebase.firestore.*
import com.sdu.moneyapp.model.User


public object UserDatabase : DatabaseManager() {
    /**
     * Registers an user with [email] and [password] a corresponding User with [name].
     * @return a success status.
     */
    public fun createUser(name: String, email: String, password: String): Boolean {
        var success = false
        AuthenticationManager.registerUser(email, password)
            .addOnSuccessListener {
                val uid = AuthenticationManager.getCurrentUserUid() ?: return@addOnSuccessListener
                updateUser(User(uid, name)).addOnSuccessListener { success = true }
            }
        return success
    }

    /**
     * Queries the DB for an User with given [uid].
     * @return a reference to the Firestore document.
     */
    public fun getUserById(userId : String) =
        getUsersCollection().document(userId).get().result.toObject(User::class.java)
    public fun updateUser(data : User) =
        getUsersCollection().document(data.uid).set(data)
}
