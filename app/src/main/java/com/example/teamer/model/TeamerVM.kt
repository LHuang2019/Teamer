package com.example.teamer.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.teamer.data.*
import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUserData : MutableLiveData<UserData> = MutableLiveData()

    private var friendList : MutableLiveData<List<UserData>> = MutableLiveData()
    private var pendingRequestsList : MutableLiveData<List<FriendRequest>> = MutableLiveData()

    var discoverProfileData : MutableLiveData<List<UserData>> = MutableLiveData()
    private var userDataRepo : UserDataRepository = UserDataRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var userLoginRepo : UserLoginRepository =
        UserLoginRepository(UserLoginDatabase.getDatabase(application).UserLoginDao())


    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUser() : FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserData() : LiveData<UserData> {
        if (auth.currentUser == null) return currentUserData

        userDataRepo.getUserByUid(auth.currentUser!!.uid)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentUserData.postValue(document.toObject(UserData::class.java))
                }
            }
            .addOnFailureListener { }
        return currentUserData
    }

    fun addNewUser(user : FirebaseUser) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener {
                userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList(), it.token))
            }
            .addOnFailureListener {
                userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList(), ""))
            }

    }

    fun addProfileData(username: String, platforms: List<Platform>, games: List<Game>) {
        userDataRepo.addProfileData(auth.currentUser!!.uid, username, platforms, games)
    }

    fun updateDiscoverProfilesData() : LiveData<List<UserData>> {

        userDataRepo.getDiscoverProfiles()
            .addOnSuccessListener { result ->
                val discoverProfiles = ArrayList<UserData>()

                for (document in result) {
                    val data = document.toObject(UserData::class.java)
                    if (data.uid != currentUserData.value?.uid) {
                        discoverProfiles.add(data)
                    }
                }

                discoverProfileData.postValue(discoverProfiles)
            }
            .addOnFailureListener { }

        return discoverProfileData
    }

    fun getCurrentUserFriendList() : LiveData<List<UserData>> {

        userDataRepo.getUserFriendList(auth.currentUser!!.uid)
            .addOnSuccessListener { query ->
                val currentFriendList = ArrayList<UserData>()

                for (document in query) {
                    val user = document.toObject(UserData::class.java)
                    currentFriendList.add(user)
                }

                friendList.postValue(currentFriendList)
            }
            .addOnFailureListener { }

        return friendList
    }

    fun getCurrentUserPendingRequests() : LiveData<List<FriendRequest>> {

        userDataRepo.getFriendRequests(auth.currentUser!!.uid)
            .addOnSuccessListener { query ->
                val currentPendingRequests = ArrayList<FriendRequest>()

                for (document in query) {
                    val friendRequest = document.toObject(FriendRequest::class.java)
                    currentPendingRequests.add(friendRequest)
                }

                pendingRequestsList.postValue(currentPendingRequests)
            }
            .addOnFailureListener { }

        return pendingRequestsList
    }

    fun addFriend(recipientId : String, sender : UserData) {
        userDataRepo.addFriend(recipientId, sender)
    }

    fun removeFriend(friendUid : String) {
        userDataRepo.removeFriend(auth.currentUser!!.uid, friendUid)
    }

    fun sendFriendRequest(recipientUid : String, recipientToken : String) {
        userDataRepo.addFriendRequest(recipientUid, recipientToken, currentUserData.value!!)
    }

    fun removeFriendRequest(documentId : String) {
        userDataRepo.removeFriendRequest(auth.currentUser!!.uid, documentId)
    }

    fun getUserLogin() : UserLogin {
        return userLoginRepo.getUserLogin()
    }

    fun updateUserLogin(email : String, password : String) = scope.launch(Dispatchers.IO) {
        userLoginRepo.clearUserLogin()
        userLoginRepo.insertUserLogin(email, password)
    }

    fun logout() {
        userLoginRepo.clearUserLogin()
        auth.signOut()
        currentUserData = MutableLiveData()
        friendList = MutableLiveData()
        pendingRequestsList = MutableLiveData()
        discoverProfileData = MutableLiveData()
    }
}