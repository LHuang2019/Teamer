package com.example.teamer.data

import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserDataRepository {

    private val userDataDao = UserDataDao()

    fun getUserByUid(uid : String) : Task<DocumentSnapshot> {
        return userDataDao.getUser(uid)
    }

    fun insertUser(user : UserData){
        userDataDao.insertUser(user)
    }

    fun addProfileData(uid: String, username: String, platforms: List<Platform>, games: List<Game>) {
        userDataDao.addProfileData(uid,
            username,
            platforms.map { platform -> platform.title },
            games.map { game -> game.title }
        )
    }

    fun getDiscoverProfiles(): Task<QuerySnapshot> {
        return userDataDao.getDiscoverProfiles()
    }
}