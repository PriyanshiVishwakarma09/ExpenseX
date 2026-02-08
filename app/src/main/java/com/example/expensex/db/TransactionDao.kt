package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(tx :TransactionEntity)

    @Query("""
        SELECT SUM(amount) FROM transactions 
        WHERE userId = :uid AND type = 'INCOME'
    """)
    suspend fun getTotalIncome(uid: String): Double?


    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE userId = :uid AND type = 'EXPENSE'
    """)
    suspend fun getTotalExpense(uid : String) : Double ?


    @Query("""
        SELECT * FROM transactions
        WHERE userId = :uid
        ORDER BY date DESC
        LIMIT 10
    """)
    suspend fun getRecent(uid : String) : List<TransactionEntity>


    @Query("""
        SELECT categoryId, SUM(amount) as total
        FROM transactions
        WHERE userId = :uid AND type = 'EXPENSE'
        GROUP BY categoryId
    """)
    suspend fun getCategoryStats(uid : String) : List<CategorySum>

}


data class CategorySum(
    val categoryId : Int ,
    val total : Double
)