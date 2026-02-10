package com.example.expensex.module

import androidx.room.Room
import com.example.expensex.db.ExpenseDatabase
import dagger.Module
import dagger.Provides
import android.content.Context
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).build()
    }

    @Provides fun provideUserDao(db: ExpenseDatabase) = db.userDao()
    @Provides fun provideAccountDao(db: ExpenseDatabase) = db.accountDao()
    @Provides fun provideCategoryDao(db: ExpenseDatabase) = db.categoryDao()
    @Provides fun provideTransactionDao(db: ExpenseDatabase) = db.transactionDao()
}
