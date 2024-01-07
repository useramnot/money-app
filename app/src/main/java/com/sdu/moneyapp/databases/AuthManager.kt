package com.sdu.moneyapp.databases

import com.google.firebase.auth.FirebaseAuth

object AuthManager {

    // Get Firebase Auth instance
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Function to register a new user with email and password
    fun registerUser(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    // Function to sign in an existing user with email and password
    fun signInUser(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    // Function to sign out the current user
    fun signOutUser() = auth.signOut()

    // Function to get the UID of the user with the given email
    /*fun getUserUid(email: String): String? {
        auth.
    }*/

    fun getCurrentUser() = auth.currentUser

    // Function to get the current user UID
    fun getCurrentUserUid(): String = auth.currentUser?.uid ?: throw Exception("User not signed in.")
    fun isUserSignedIn(): Boolean = auth.currentUser != null
}
