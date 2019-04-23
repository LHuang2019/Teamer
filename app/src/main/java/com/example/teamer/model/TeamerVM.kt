package com.example.teamer.model

import android.app.Activity
import android.app.Application
import android.content.*
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.teamer.data.*
import com.example.teamer.service.FriendRequestService
import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TeamerVM(application : Application) : AndroidViewModel(application) {

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null
    private var currentUserData : MutableLiveData<UserData> = MutableLiveData()
    private var friendList : MutableLiveData<List<UserData>> = MutableLiveData()
    var discoverProfileData : MutableLiveData<List<UserData>> = MutableLiveData()
    private var userDataRepo : UserDataRepository = UserDataRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var userLoginRepo : UserLoginRepository =
        UserLoginRepository(UserLoginDatabase.getDatabase(application).UserLoginDao())

    // service variables
    private var friendRequestService: FriendRequestService? = null
    var isBound = false

    private val messagingServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as FriendRequestService.ServiceBinder
            friendRequestService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            friendRequestService = null
            isBound = false
        }
    }

    fun bindMessagingService(activity: Activity, context: Context) {
        if (!isBound) {
            val serviceIntent = Intent(Intent(context, FriendRequestService::class.java))
            serviceIntent.putExtra(FriendRequestService.USER_INTENT, currentUserData.value?.uid)
            activity.bindService(serviceIntent, messagingServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun getAuth() : FirebaseAuth {
        return auth
    }

    fun getCurrentUserData() : LiveData<UserData> {
        if (currentUser == null) return currentUserData
        userDataRepo.getUserByUid(currentUser!!.uid)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentUserData.postValue(document.toObject(UserData::class.java))
                }
            }
            .addOnFailureListener { }
        return currentUserData
    }

    fun setCurrentUser(user : FirebaseUser) {
        currentUser = user
    }

    fun addNewUser(user : FirebaseUser) {
        userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList()))
    }

    fun addProfileData(username: String, platforms: List<Platform>, games: List<Game>) {
        userDataRepo.addProfileData(currentUser!!.uid, username, platforms, games)
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

        userDataRepo.getUserFriendList(currentUser!!.uid)
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

    fun sendFriendRequest(recipientId : String) {
        userDataRepo.addFriendRequest(recipientId, currentUserData.value!!)
    }

    fun removeFriendRequest(documentId : String) {
        userDataRepo.removeFriendRequest(documentId)
    }

    fun addFriend(recipientId : String, sender : UserData) {
        userDataRepo.addFriend(recipientId, sender)
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
        currentUser = null
        currentUserData = MutableLiveData()
        friendList = MutableLiveData()
        discoverProfileData = MutableLiveData()
    }
}