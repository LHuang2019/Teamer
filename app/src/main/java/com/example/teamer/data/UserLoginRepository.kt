package com.example.teamer.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserLoginRepository(private val userLoginDao : UserLoginDao) {

    fun getUserLogin() : UserLogin {
        return userLoginDao.getUserLogin()
    }

    @WorkerThread
    fun insertUserLogin(email : String, password : String) {
        userLoginDao.insertUserLogin(UserLogin(email, password))
    }

    @WorkerThread
    fun clearUserLogin() {
        userLoginDao.clear()
    }
}