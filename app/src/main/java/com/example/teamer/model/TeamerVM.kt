package com.example.teamer.model

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.teamer.data.*
import com.example.teamer.misc.Game
import com.example.teamer.misc.Platform
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
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

    private var coroutineJob : Job? = null
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private var geocoder = Geocoder(application, Locale.getDefault())

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
                userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList(), "", it.token))
                    .addOnSuccessListener {
                        addLocation()
                    }
            }
            .addOnFailureListener {
                userDataRepo.insertUser(UserData(user.uid, "", user.email!!, emptyList(), emptyList(), "", ""))
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
                    if (data.uid != currentUserData.value?.uid && data !in friendList.value!!) {
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

    @SuppressLint("MissingPermission")
    fun addLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    coroutineJob?.cancel()

                    coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                        val addressDeferred = async {
                            getAddress(location)
                        }
                        val address = addressDeferred.await()
                        withContext(Dispatchers.Main) {
                            userDataRepo.addUserLocation(auth.currentUser!!.uid, address)
                        }
                    }

                }
            }
    }

    private fun getAddress(location : Location) : String {
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude,1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            return "Error: Service Not Available --$ioException"

        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            return "Error: Invalid lat long used--$illegalArgumentException"
        }

        if(addresses.isEmpty())
            return ""

        return "${addresses[0].locality}, ${addresses[0].postalCode}"
    }
}