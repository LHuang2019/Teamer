package com.example.teamer.data

data class UserData(
    var uid : String,
    var username : String,
    var email : String,
    var platforms : List<String>,
    var games : List<String>
) {
    constructor() : this("", "", "", emptyList<String>(), emptyList<String>())
}