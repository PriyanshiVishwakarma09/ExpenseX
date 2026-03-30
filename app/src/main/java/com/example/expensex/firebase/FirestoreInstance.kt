package com.example.expensex.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreInstance {
    val db = FirebaseFirestore.getInstance()

    fun userDoc(userId: String) =
        db.collection("users").document(userId)
}