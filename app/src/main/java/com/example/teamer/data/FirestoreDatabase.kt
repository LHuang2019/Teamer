package com.example.teamer.data

import com.google.firebase.firestore.FirebaseFirestore

abstract class FirestoreDatabase {

    companion object {
        fun getDatabase() : FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }
}