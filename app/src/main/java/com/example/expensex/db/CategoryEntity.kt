package com.example.expensex.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val name: String,  // Food , Salary , Travel
    val type: String // "Income or Expense "
)
