package com.example.teamer.data


import java.io.Serializable

data class MessageData(
    var sender_uid: String,
    var recipient_uid: String,
    var messageText : String,
    var timestamp: Long
) : Serializable {
    constructor() : this("", "", "", 0)
}