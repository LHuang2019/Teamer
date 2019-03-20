package com.example.teamer

import androidx.lifecycle.LiveData

class UserDataRepository {

    private val userDataDao = UserDataDao()

    fun getUserByUid(uid : String) : LiveData<UserData> {
        return userDataDao.getUser(uid)
    }

    fun insertUser(user : UserData){
        userDataDao.insertUser(user)
    }

    fun addProfileData(uid: String, username: String, platforms: ArrayList<String>, games: ArrayList<String>) {
        userDataDao.addProfileData(uid, username, platforms, games)
    }
}