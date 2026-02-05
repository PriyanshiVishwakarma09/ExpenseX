package com.example.expensex.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CategoryDao {
  @Insert
  suspend fun insert(category: CategoryEntity)

  @Query("SELECT * FROM categories WHERE userId = :uid AND type = :type")
  suspend fun getCategories(uid : String , type : String) : List<CategoryEntity>
}

