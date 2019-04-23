package com.example.teamer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserLoginDao {

    @Query("SELECT * FROM user_login_table LIMIT 1")
    fun getUserLogin() : UserLogin

    @Insert
    fun insertUserLogin(userLogin : UserLogin)

    @Query("DELETE FROM user_login_table")
    fun clear()
}