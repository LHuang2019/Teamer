package com.example.teamer.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserDataDao {

    private val db = FirestoreDatabase.getDatabase()

    companion object {
        private const val COLLECTION_NAME = "users"
        private const val FRIEND_LIST_COLLECTION = "friends"
    }

    fun getUser(uid : String) : Task<DocumentSnapshot> {
         return db.collection(COLLECTION_NAME).document(uid).get()
    }

    fun insertUser(user : UserData) {
        db.collection(COLLECTION_NAME).document(user.uid).set(user)
    }

    fun addProfileData(uid: String, username: String, platforms: List<String>, games: List<String>) {
        db.collection(COLLECTION_NAME).document(uid).update(
            "username", username,
            "platforms", platforms,
            "games", games
        )
    }

    fun getUserFriendList(uid : String): Task<QuerySnapshot> {
        return db.collection(COLLECTION_NAME)
            .document(uid).collection(FRIEND_LIST_COLLECTION).get()
    }

    fun getDiscoverProfiles(): Task<QuerySnapshot> {
        // get 3 user profiles in the DB
        return db.collection(COLLECTION_NAME).limit(4).get()
    }
}