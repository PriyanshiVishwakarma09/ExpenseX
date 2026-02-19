package com.example.expensex.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        userEntity::class,
    AccountEntity::class ,
    CategoryEntity::class,
    TransactionEntity::class
    ],
    version = 2
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}