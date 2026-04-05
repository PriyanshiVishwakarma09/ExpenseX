package com.example.expensex.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun startBudgetWorker(context: Context) {

    val request = PeriodicWorkRequestBuilder<BudgetWorker>(
        6, TimeUnit.HOURS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "budget_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}