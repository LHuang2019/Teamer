package com.example.teamer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null
    private var currentUserData : UserData? = null

    private var userDataRepo : UserDataRepository = UserDataRepository()

    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUser() : FirebaseUser? {
        return currentUser
    }

    fun setCurrentUser(user : FirebaseUser) {
        currentUser = user
        userDataRepo.getUserByUid(user.uid)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentUserData = UserData(
                        document["uid"].toString(),
                        document["username"].toString(),
                        document["email"].toString()
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore Error", "Error getting documents.", exception)
            }
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!))
    }
}