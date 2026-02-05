package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(account : AccountEntity)

    @Query("SELECT * FROM account WHERE userId = :uid")
    suspend fun getAccounts(uid: String): List<AccountEntity>

    @Query("UPDATE account SET balance = balance + :amount Where id = :accountId")
    suspend fun updateBalance(accountId : Int , amount : Double)
}