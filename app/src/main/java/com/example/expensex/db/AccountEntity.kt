package com.example.expensex.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "account",
    indices = [Index(value = ["userId" ,"isMain"] , unique = true)]
)
data class AccountEntity(
    @PrimaryKey (autoGenerate = true) val id : Int = 0,
    val userId: String,
    val name : String ,  // cash , Sbi , paytm
    val balance : Double,
    val isMain: Boolean = false
)
