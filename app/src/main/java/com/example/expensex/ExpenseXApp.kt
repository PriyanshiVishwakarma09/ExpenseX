package com.example.expensex

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExpenseXApp : Application(), Configuration.Provider {


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() {
            android.util.Log.d("HILT", "Custom WorkerFactory used")
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        }
}