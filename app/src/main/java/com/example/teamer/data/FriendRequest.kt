package com.example.teamer.data

import java.io.Serializable

data class FriendRequest (
    val uid : String,
    val sender : UserData,
    val recipientUid : String,
    val recipientToken : String
) : Serializable {
    constructor() : this("", UserData(), "", "")
}