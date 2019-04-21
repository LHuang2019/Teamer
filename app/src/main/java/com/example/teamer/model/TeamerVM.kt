package com.example.teamer.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.teamer.data.UserData
import com.example.teamer.data.UserDataRepository
import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null
    private var currentUserData : MutableLiveData<UserData> = MutableLiveData()

    private var discoverProfileData : MutableLiveData<ArrayList<UserData>> = MutableLiveData()

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
                    currentUserData.postValue(document.toObject(UserData::class.java))
                }
            }
            .addOnFailureListener { }
        return currentUserData
    }

    fun setCurrentUser(user : FirebaseUser) {
        currentUser = user
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList(), emptyList()))
    }

    fun addProfileData(username: String, platforms: List<Platform>, games: List<Game>) {
        userDataRepo.addProfileData(currentUser!!.uid, username, platforms, games)
    }

    fun getDiscoverProfilesData() : LiveData<ArrayList<UserData>> {
        val discoverProfiles = ArrayList<UserData>()

        userDataRepo.getDiscoverProfiles()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.toObject(UserData::class.java)
                    if (data.uid != currentUserData.value?.uid) {
                        discoverProfiles.add(data)
                    }
                }

                discoverProfileData.postValue(discoverProfiles)
            }
            .addOnFailureListener { }

        return discoverProfileData
    }
}