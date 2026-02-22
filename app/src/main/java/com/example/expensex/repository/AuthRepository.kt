package com.example.expensex.repository

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensex.db.UserDao
import com.example.expensex.db.userEntity
import com.example.expensex.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore,
    private val userDao: UserDao
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
                val users = userEntity(
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

                CoroutineScope(Dispatchers.IO).launch{
                    userDao.insertUser(users)
                    Log.d("User" ,"User data inserted" )
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