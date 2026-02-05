package com.example.expensex.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class userEntity (
    @PrimaryKey val uid : String,
    val name :String ,
    val email : String
)