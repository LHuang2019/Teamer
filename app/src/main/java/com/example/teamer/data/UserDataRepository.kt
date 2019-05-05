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

    fun insertUser(user : UserData): Task<Void> {
        return userDataDao.insertUser(user)
    }

    fun addProfileData(uid: String, username: String, platforms: List<Platform>, games: List<Game>) {
        userDataDao.addProfileData(uid,
            username,
            platforms.map { platform -> platform.title },
            games.map { game -> game.title }
        )
    }

    fun addUserLocation(uid : String, location : String) {
        userDataDao.addUserLocation(uid, location)
    }

    fun getUserFriendList(uid : String): Task<QuerySnapshot> {
        return userDataDao.getUserFriendList(uid)
    }

    fun addFriend(recipient : String, sender : UserData) {
        userDataDao.addFriend(recipient, sender)
    }

    fun removeFriend(userUid : String, friendUid : String) {
        userDataDao.removeFriend(userUid, friendUid)
    }

    fun getFriendRequests(uid : String): Task<QuerySnapshot> {
        return userDataDao.getPendingFriendRequests(uid)
    }

    fun addFriendRequest(recipientUid : String, recipientToken : String, sender : UserData) {
        userDataDao.addFriendRequest(recipientUid, recipientToken, sender)
    }

    fun removeFriendRequest(uid : String, documentId : String) {
        userDataDao.removeFriendRequest(uid, documentId)
    }

    fun getDiscoverProfiles(): Task<QuerySnapshot> {
        return userDataDao.getDiscoverProfiles()
    }
}