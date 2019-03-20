package com.example.teamer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null

    private var userDataRepo : UserDataRepository = UserDataRepository()

    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUser() : FirebaseUser? {
        return currentUser
    }

    fun getCurrentUserData() : LiveData<UserData> {
        return userDataRepo.getUserByUid(currentUser!!.uid)

    }

    fun setCurrentUser(user : FirebaseUser) {
        currentUser = user
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!, ArrayList(), ArrayList()))
    }

    fun addProfileData(username: String, platforms: ArrayList<String>, games: ArrayList<String>) {
        userDataRepo.addProfileData(currentUser!!.uid, username, platforms, games)
    }
}