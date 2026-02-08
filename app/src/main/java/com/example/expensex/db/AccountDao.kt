package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(account : AccountEntity)

    // User normally has only ONE main account
    @Query("SELECT * FROM account WHERE userId = :uid")
    suspend fun getMainAccounts(uid: String): AccountEntity?

    // Add income OR add balance
    @Query("UPDATE account SET balance = balance + :amount Where id = :accountId")
    suspend fun addBalance(accountId : Int , amount : Double)

    //subtract expense
    @Query("UPDATE account SET balance = balance - :amount WHere id = :accountId")
    suspend fun subtractBalance(accountId: Int , amount: Double)

}