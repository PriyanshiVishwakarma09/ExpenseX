package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(tx :TransactionEntity)

    @Query("""
        SELECT SUM(amount) FROM transactions 
        WHERE userId = :uid AND type = 'INCOME'
    """)
    fun getTotalIncome(uid: String): Flow<Double?>


    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE userId = :uid AND type = 'EXPENSE'
    """)
    fun getTotalExpense(uid : String) : Flow<Double?>


    @Query("""
        SELECT * FROM transactions
        WHERE userId = :uid
        ORDER BY date DESC
        LIMIT 10
    """)
    fun getRecent(uid : String) : Flow<List<TransactionEntity>>


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