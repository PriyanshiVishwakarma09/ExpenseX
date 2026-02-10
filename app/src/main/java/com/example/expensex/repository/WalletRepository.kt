package com.example.expensex.repository

import com.example.expensex.db.AccountDao
import com.example.expensex.db.TransactionDao
import com.example.expensex.db.TransactionEntity
import jakarta.inject.Inject

class WalletRepository @Inject constructor (
    private val accountDao: AccountDao ,
    private val transactionDao: TransactionDao
){
    suspend fun getMainAccount(uid : String) = accountDao.getMainAccounts(uid)

    suspend fun addIncome(
        uid : String ,
        title : String ,
        amount : Double,
        accountId : Int
    ){
        val tx = TransactionEntity(
            userId = uid ,
            title = title ,
            amount = amount ,
            type = "INCOME" ,
            date = System.currentTimeMillis(),
            accountId = accountId ,
            categoryId = null
        )

        transactionDao.insert(tx)
        accountDao.addBalance(accountId , amount)
    }
}