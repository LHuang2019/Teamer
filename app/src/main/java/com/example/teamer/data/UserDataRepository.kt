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

    fun getUserFriendList(uid : String): Task<QuerySnapshot> {
        return userDataDao.getUserFriendList(uid)
    }

    fun addFriendRequest(recipient : String, sender : UserData) {
        userDataDao.addFriendRequest(recipient, sender)
    }

    fun removeFriendRequest(documentId : String) {
        userDataDao.removeFriendRequest(documentId)
    }

    fun addFriend(recipient : String, sender : UserData) {
        userDataDao.addFriend(recipient, sender)
    }

    fun getDiscoverProfiles(): Task<QuerySnapshot> {
        return userDataDao.getDiscoverProfiles()
    }
}