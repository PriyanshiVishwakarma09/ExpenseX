package com.example.expensex.module

import com.example.expensex.SessionManager
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore() : FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideSession(@ApplicationContext context: Context) =
        SessionManager(context)
}
