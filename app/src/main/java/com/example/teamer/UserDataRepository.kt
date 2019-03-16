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
}