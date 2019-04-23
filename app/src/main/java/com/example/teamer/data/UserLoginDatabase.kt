package com.example.teamer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserLogin::class], version = 1, exportSchema = false)
abstract class UserLoginDatabase : RoomDatabase() {

    abstract fun UserLoginDao(): UserLoginDao

    companion object {
        @Volatile
        private var INSTANCE: UserLoginDatabase? = null

        fun getDatabase(
            context : Context
        ): UserLoginDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserLoginDatabase::class.java,
                    "user_login_database"
                ).allowMainThreadQueries().
                    build()
                INSTANCE = instance
                return instance
            }
        }
    }
}