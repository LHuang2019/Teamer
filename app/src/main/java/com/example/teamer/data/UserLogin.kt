package com.example.teamer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_login_table")
data class UserLogin (@PrimaryKey @ColumnInfo(name =" email") var email : String,
                      @ColumnInfo(name = "password") var password : String
)