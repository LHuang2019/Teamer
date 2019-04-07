package com.example.teamer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null
    private var currentUserData : MutableLiveData<UserData> = MutableLiveData()

    private var userDataRepo : UserDataRepository = UserDataRepository()


    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUser() : FirebaseUser? {
        return currentUser
    }

    fun getCurrentUserData() : LiveData<UserData> {
        userDataRepo.getUserByUid(currentUser!!.uid)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentUserData.postValue(UserData(
                        document["uid"].toString(),
                        document["username"].toString(),
                        document["email"].toString(),
                        document["platforms"] as ArrayList<String>,
                        document["games"] as ArrayList<String>
                    ))
                }
            }
            .addOnFailureListener { exception ->
            }
        return currentUserData
    }

    fun setCurrentUser(user : FirebaseUser) {
        currentUser = user
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!, ArrayList(), ArrayList()))
    }

    fun addProfileData(username: String, platforms: List<Platform>, games: List<Game>) {
        userDataRepo.addProfileData(currentUser!!.uid, username, platforms, games)
    }
}