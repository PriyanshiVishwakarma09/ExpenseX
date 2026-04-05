package com.example.expensex.worker

import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expensex.SessionManager
import com.example.expensex.db.BudgetDao
import com.example.expensex.db.TransactionDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.expensex.R
import java.time.LocalDate


@HiltWorker
class BudgetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val session: SessionManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("worker", "🔥 Worker Started!")

        val uid = session.getUid() ?: return Result.success()
        val currentDate = LocalDate.now()
        val currentMonth = currentDate.monthValue // Example: April = 4
        val currentYear = currentDate.year

        // 2. Use your existing getBudget function with the current date
        val budget = budgetDao.getBudget(currentMonth, currentYear)?.amount ?: 0.0

        // 3. Get expenses (Note: This still gets ALL expenses forever, we'll fix that later)
        val expense = transactionDao.getMonthlyExpenseOnce(uid) ?: 0.0

        Log.d("worker", "Fetched -> Expense: $expense, Budget: $budget")

        if (budget == 0.0) {
            Log.d("worker", "Budget is 0 for $currentMonth/$currentYear. Exiting silently.")
            return Result.success()
        }

        val percent = (expense / budget) * 100
        Log.d("worker", "Calculated Percent: $percent%")

        val message = when {
            percent >= 100 -> "🚨 Budget exceeded by ₹${(expense - budget).toInt()}!"
            percent >= 90 -> "⚠️ 90% of budget used"
            percent >= 70 -> "⚠️ 70% of budget used"
            else -> null
        }

        message?.let { showNotification(it) }

        return Result.success()
    }

    private fun showNotification(msg: String) {
        try {
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "budget_alerts"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Budget Alerts", NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle("ExpenseX Alert")
                .setContentText(msg)
                // 🚨 Use your own app icon here
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()

            manager.notify(System.currentTimeMillis().toInt(), notification)
            Log.d("worker", "✅ Notification pushed to Android System!")
        } catch (e: Exception) {
            Log.e("worker", "❌ Notification crashed: ${e.message}")
        }
    }
}