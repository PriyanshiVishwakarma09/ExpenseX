package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(category: CategoryEntity)

  @Delete
  suspend fun delete(category: CategoryEntity)

  @Query("SELECT * FROM categories WHERE userId = :uid AND type = :type")
   fun getCategories(uid : String , type : String) : Flow<List<CategoryEntity>>
}

