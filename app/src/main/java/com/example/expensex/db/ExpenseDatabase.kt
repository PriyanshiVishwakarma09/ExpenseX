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
    version = 1
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

//    companion object {
//        @Volatile private var INSTANCE: ExpenseDatabase? = null
//        fun getInstance(context: Context): ExpenseDatabase {
//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(
//                    context.applicationContext,
//                    ExpenseDatabase::class.java,
//                    "expense_db"
//                ).build().also { INSTANCE = it }
//            }
//        }
//    }
}