package com.sdu.moneyapp.databases

import com.google.firebase.firestore.*
import com.sdu.moneyapp.model.User


public object UserDatabase : DatabaseManager() {
    private val savedUsers = mutableMapOf<String, User>()
    private val savedUsersAge = mutableMapOf<String, Long>()


    /**
     * Registers an user with [email] and [password] a corresponding User with [name].
     * @return a success status.
     */
    fun createUser(name: String, email: String, password: String, callbackSuccess: () -> Unit = {}, callbackError: (String) -> Unit = {}){
        AuthManager.registerUser(email, password)
            .addOnSuccessListener {
                setUser(User(AuthManager.getCurrentUserUid(), name, AuthManager.getCurrentUserEmail()),{
                    callbackSuccess()
                },
                {
                    callbackError(it)
                })
            }.addOnFailureListener {
                callbackError(it.message ?: "Unknown error")
                throw Exception("User $name failed to be created: " + it.message)
            }
    }

    /**
     * Queries the DB for an User with given [uid].
     * @return a reference to the Firestore document.
     */
    fun getUserById(userId : String, callback: (User) -> Unit = {}) {
        if (savedUsers.containsKey(userId)) {
            if (System.currentTimeMillis() - savedUsersAge.getValue(userId) > SUITABLE_AGE) {
                savedUsers.remove(userId)
                savedUsersAge.remove(userId)
            } else {
                callback(savedUsers.getValue(userId))
            }
        }
        getUsersCollection().document(userId).get()
            .addOnSuccessListener{
                val user = it.toObject(User::class.java) ?: throw Exception("User $userId failed to be found: Object is null")
                savedUsers[userId] = user
                savedUsersAge[userId] = System.currentTimeMillis()
                callback(user)
            }
            .addOnFailureListener {throw Exception("User $userId failed to be found: " + it.message)}
    }

    fun getUserByEmail(userEmail : String, callback: (User?) -> Unit = {}) {
        getUsersCollection().whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { documents ->
                if (documents.size() == 0) callback(null)
                else {
                    val user = documents.elementAt(0).toObject(User::class.java)
                    callback(user)
                }
            }
            .addOnFailureListener {throw Exception("User $userEmail failed to be found: " + it.message)}
    }

    fun setUser(data : User, callbackSuccess: () -> Unit = {}, callbackError: (String) -> Unit = {}) {
        getUsersCollection().document(data.uid).set(data)
            .addOnSuccessListener {
                savedUsers[data.uid] = data
                savedUsersAge[data.uid] = System.currentTimeMillis()
                callbackSuccess()
            }
            .addOnFailureListener {
                callbackError(it.message ?: "Unknown error")
                throw Exception("User " + data.uid + " failed to set: " + it.message)
            }
    }
}
