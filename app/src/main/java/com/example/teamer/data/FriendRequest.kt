package com.example.teamer.data

import java.io.Serializable

data class FriendRequest (
    var sender : UserData,
    var recipient : String
) : Serializable {
    constructor() : this(UserData(), "")
}