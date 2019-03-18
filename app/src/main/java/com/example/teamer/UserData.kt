package com.example.teamer

data class UserData(
    var uid : String,
    var username : String,
    var email : String,
    var platforms : ArrayList<String>,
    var games : ArrayList<String>
)