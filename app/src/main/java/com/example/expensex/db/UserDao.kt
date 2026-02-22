package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Insert this data. If a row with the same primary key already exists, replace it with the new one.
    suspend fun insertUser(user : userEntity)

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1") //Find the row where table.uid == function.uid. return only one row even if multiple matches
    suspend fun getUser(uid : String) : userEntity ? //It fetches a single user from the database whose uid you pass.
//    Look in the users table
//    Find the row where uid = "abc123"
//    Return that row as a UserEntity object
//    If no such user exists → returns null

}