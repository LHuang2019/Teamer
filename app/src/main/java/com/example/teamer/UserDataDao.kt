package com.example.teamer

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot

class UserDataDao {

    private val db = FirestoreDatabase.getDatabase()

    companion object {
        private const val COLLECTION_NAME = "users"
    }

    fun getUser(uid : String) : Task<DocumentSnapshot> {
        return db.collection(COLLECTION_NAME).document(uid).get()
    }

    fun insertUser(user : UserData) {
        val userData = HashMap<String, Any>()
        userData["uid"] = user.uid
        userData["username"] = user.username
        userData["email"] = user.email
        userData["platforms"] = user.platforms
        userData["games"] = user.games

        db.collection(COLLECTION_NAME).document(user.uid).set(userData)
    }

    fun addProfileData(uid: String, username: String, platforms: ArrayList<String>, games: ArrayList<String>) {
        db.collection(COLLECTION_NAME).document(uid).update(
            "username", username,
            "platforms", platforms,
            "games", games
        )
    }
}