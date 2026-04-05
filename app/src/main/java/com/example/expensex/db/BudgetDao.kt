package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.Year

@Dao
interface BudgetDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: MonthlyBudget)

    @Query("""
        SELECT * FROM monthly_budget
        WHERE month = :month AND year = :year
        LIMIT 1
    """)
    suspend fun getBudget(month: Int , year: Int): MonthlyBudget?

    @Update
    suspend fun updateBudget(budget: MonthlyBudget)

    @Delete
    suspend fun deleteBudget(budget: MonthlyBudget)

    @Query("SELECT * FROM monthly_budget WHERE Id = :uid LIMIT 1")
    suspend fun getCurrentBudgetOnce(uid: String): MonthlyBudget?
}