package com.example.expensex.repository

import android.util.Log
import com.example.expensex.db.AccountDao
import com.example.expensex.db.AccountEntity
import com.example.expensex.db.CategoryDao
import com.example.expensex.db.CategoryEntity
import com.example.expensex.db.TransactionDao
import com.example.expensex.db.TransactionEntity
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) {

    suspend fun getMainAccount(uid: String) =
        accountDao.getMainAccounts(uid)

    suspend fun ensureMainAccount(uid: String) {
        val existing = accountDao.getMainAccounts(uid)
        if (existing != null) {
            Log.d("DB", "Main account already exists")
            return
        }
        try {
            accountDao.insert(
                AccountEntity(
                    userId = uid,
                    name = "Main Account",
                    balance = 0.0,
                    isMain = true
                )
            )
            Log.d("DB", "✅ Main account created")
        } catch (e: Exception) {
            Log.d("DB", "⚠️ Duplicate prevented")
        }
    }

    suspend fun addIncome(
        uid: String,
        title: String,
        amount: Double,
        accountId: Int,
        categoryId: Int?
    ) {
        val tx = TransactionEntity(
            userId = uid,
            title = title,
            amount = amount,
            type = "INCOME",
            date = System.currentTimeMillis(),
            accountId = accountId,
            categoryId = categoryId
        )
        transactionDao.insert(tx)
        accountDao.addBalance(accountId, amount)
    }

    suspend fun addExpense(
        uid: String,
        title: String,
        amount: Double,
        accountId: Int,
        categoryId: Int
    ) {

        Log.d("DB", "INSERT CALLED: $title $amount $categoryId")
        val tx = TransactionEntity(
            userId = uid,
            title = title,
            amount = amount,
            type = "EXPENSE",
            date = System.currentTimeMillis(),
            accountId = accountId,
            categoryId = categoryId
        )
        transactionDao.insert(tx)
        accountDao.subtractBalance(accountId, amount)
    }

    fun getCategories(uid: String, type: String) =
        categoryDao.getCategories(uid, type)

    suspend fun addCategory(cat: CategoryEntity) =
        categoryDao.insert(cat)

    suspend fun deleteCategory(cat: CategoryEntity) =
        categoryDao.delete(cat)
}



