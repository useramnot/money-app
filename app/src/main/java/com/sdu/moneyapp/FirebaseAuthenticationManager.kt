package com.sdu.moneyapp

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthenticationManager {

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

    // Function to get the current user UID
    fun getCurrentUserUid(): String? = auth.currentUser?.uid
}
