package com.example.expensex.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey (autoGenerate = true) val id : Int = 0,
    val userId: String,
    val name : String ,  // cash , Sbi , paytm
    val balance : Double
)
