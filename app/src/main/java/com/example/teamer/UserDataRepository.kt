package com.example.teamer

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class UserDataRepository {

    private val userDataDao = UserDataDao()

    fun getUserByUid(uid : String) : Task<DocumentSnapshot> {
        return userDataDao.getUser(uid)
    }

    fun insertUser(user : UserData){
        userDataDao.insertUser(user)
    }

    fun addProfileData(uid: String, username: String, platforms: ArrayList<String>, games: ArrayList<String>) {
        userDataDao.addProfileData(uid, username, platforms, games)
    }
}