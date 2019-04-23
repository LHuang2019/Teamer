package com.example.teamer.data

import java.io.Serializable

data class FriendRequest (
    val uid : String,
    val sender : UserData,
    val recipient : String
) : Serializable {
    constructor() : this("", UserData(), "")
}