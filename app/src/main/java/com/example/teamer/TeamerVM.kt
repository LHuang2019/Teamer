package com.example.teamer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null
    var currentUserData : UserData? = null

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
                        document["email"].toString(),
                        document["platforms"] as ArrayList<String>,
                        document["games"] as ArrayList<String>
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore Error", "Error getting documents.", exception)
            }
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!, ArrayList<String>(), ArrayList<String>()))
    }

    fun addProfileData(username: String, platforms: ArrayList<String>, games: ArrayList<String>) {
        currentUserData?.username = username
        currentUserData?.platforms = platforms
        currentUserData?.games = games
        userDataRepo.addProfileData(currentUser!!.uid, username, platforms, games)
    }
}