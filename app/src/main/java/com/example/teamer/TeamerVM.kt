package com.example.teamer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null

    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUser() : FirebaseUser? {
        return currentUser
    }

    fun setCurrentUser(user : FirebaseUser?) {
        currentUser = user
    }
}