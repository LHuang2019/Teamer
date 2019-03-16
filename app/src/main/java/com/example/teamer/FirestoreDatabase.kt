package com.example.teamer

import com.google.firebase.firestore.FirebaseFirestore

abstract class FirestoreDatabase {

    companion object {
        fun getDatabase() : FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }
}