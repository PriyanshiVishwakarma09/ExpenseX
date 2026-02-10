package com.example.expensex.repository

import com.example.expensex.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore
){    fun register(
        name : String ,
        email : String ,
        password : String ,
        onResult: (Boolean , String) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email , password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid

                val user = User(
                    uid = uid,
                    name = name,
                    email = email
                )

                db.collection("users").document(uid).set(user)
                    .addOnSuccessListener {
                        onResult(true , "Account created successfully")
                    }
                    .addOnFailureListener{
                        onResult(false, it.message ?: "Firestore error") //if firebase gives an error then use that otherwise return Firestore Error
                    }
            }
            .addOnFailureListener{
                onResult(false , it.message ?: "Authentication failed")
            }
    }
    fun login(
        email : String ,
        password: String ,
        onResult: (Boolean, String) -> Unit
    ){
        auth.signInWithEmailAndPassword(email , password)
            .addOnSuccessListener { onResult(true , "Login success")}
            .addOnFailureListener { onResult(false , it.message ?: "Login failed")
            }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser //This function returns the currently logged-in Firebase user, or null if no user is logged in.
}