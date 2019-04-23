package com.example.teamer.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserDataDao {

    private val db = FirestoreDatabase.getDatabase()

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FRIEND_REQUESTS_COLLECTION = "friend_requests"
        private const val FRIEND_LIST_COLLECTION = "friends"
    }

    fun getUser(uid : String) : Task<DocumentSnapshot> {
         return db.collection(USERS_COLLECTION).document(uid).get()
    }

    fun insertUser(user : UserData) {
        db.collection(USERS_COLLECTION).document(user.uid).set(user)
    }

    fun addProfileData(uid: String, username: String, platforms: List<String>, games: List<String>) {
        db.collection(USERS_COLLECTION).document(uid).update(
            "username", username,
            "platforms", platforms,
            "games", games
        )
    }

    fun getUserFriendList(uid : String): Task<QuerySnapshot> {
        return db.collection(USERS_COLLECTION)
            .document(uid).collection(FRIEND_LIST_COLLECTION).get()
    }

    fun addFriendRequest(recipient : String, sender : UserData) {
        val ref = db.collection(FRIEND_REQUESTS_COLLECTION).document()
        val friendRequest = FriendRequest(ref.id, sender, recipient)
        ref.set(friendRequest)
    }

    fun addFriend(recipient : String, sender : UserData) {
        db.collection(USERS_COLLECTION).document(recipient).collection(FRIEND_LIST_COLLECTION).add(sender)
        getUser(recipient).addOnSuccessListener { document ->
            if (document.exists()) {
                val userData = document.toObject(UserData::class.java)
                userData?.let {
                    db.collection(USERS_COLLECTION).document(sender.uid).collection(FRIEND_LIST_COLLECTION).add(
                        it
                    )
                }
            }
        }.addOnFailureListener { }
    }

    fun removeFriendRequest(documentId : String) {
        db.collection(FRIEND_REQUESTS_COLLECTION).document(documentId).delete()
    }

    fun getDiscoverProfiles(): Task<QuerySnapshot> {
        // get 3 user profiles in the DB
        return db.collection(USERS_COLLECTION).limit(4).get()
    }
}