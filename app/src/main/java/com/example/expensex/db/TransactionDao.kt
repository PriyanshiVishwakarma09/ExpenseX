package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.expensex.model.TimePeriodSum
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
     fun getCategoryStats(uid : String) : Flow<List<CategorySum>>

     @Query("""
         SELECT strftime('%Y-%m-%d' , date /1000 , 'unixepoch' , 'localtime') as timeLabel ,
         SUM(amount) as total
         FROM transactions
         WHERE userId = :uid 
         AND type = 'EXPENSE'
         AND date >= :startDate
         AND date <= :endDate
         GROUP BY timeLabel
         ORDER BY timeLabel ASC
     """)
     fun getExpensesPerDay(uid : String , startDate : Long , endDate : Long) : Flow<List<TimePeriodSum>>

    @Query("""
    SELECT strftime('%m', date / 1000, 'unixepoch', 'localtime') as timeLabel,
    SUM(amount) as total
    FROM transactions
    WHERE userId = :uid 
    AND type = 'EXPENSE'
    AND date >= :startDate
    AND date <= :endDate
    GROUP BY timeLabel
    ORDER BY timeLabel ASC
    """)
    fun getExpensesPerMonth(
        uid: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<TimePeriodSum>>

    @Query("""
         SELECT strftime('%Y-%m-%d' , date /1000 , 'unixepoch' , 'localtime') as timeLabel ,
         SUM(amount) as total
         FROM transactions
         WHERE userId = :uid 
         AND type = 'EXPENSE'
         GROUP BY timeLabel
         ORDER BY timeLabel ASC
     """)
    fun getExpensesPerYear(uid : String) : Flow<List<TimePeriodSum>>


    @Query("""
    SELECT IFNULL(SUM(amount), 0.0) FROM transactions
    WHERE type = 'EXPENSE'
    AND strftime('%m', date / 1000, 'unixepoch') = :month
    AND strftime('%Y', date / 1000, 'unixepoch') = :year
""")
    suspend fun getMonthlyExpenses(month: String, year: String): Double
}

data class CategorySum(
    val categoryId : Int ,
    val total : Double
)