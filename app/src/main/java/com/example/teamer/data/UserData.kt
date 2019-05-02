package com.example.teamer.data

import java.io.Serializable

data class UserData(
    var uid : String,
    var username : String,
    var email : String,
    var platforms : List<String>,
    var games : List<String>,
    var tokenId : String
) : Serializable {
    constructor() : this("", "", "", emptyList(), emptyList(), "")
}