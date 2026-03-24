package com.example.expensex.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monthly_budget",
    indices = [Index(value = ["month" , "year"] , unique = true)]
)
data class MonthlyBudget(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0 ,
    val amount : Double ,
    val month : Int ,
    val year : Int
)