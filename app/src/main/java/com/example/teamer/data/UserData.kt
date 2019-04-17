package com.example.teamer.data

data class UserData(
    var uid : String,
    var username : String,
    var email : String,
    var platforms : List<String>,
    var games : List<String>,
    var friendList : List<UserData>
) {
    constructor() : this("", "", "", emptyList(), emptyList(), emptyList())
}