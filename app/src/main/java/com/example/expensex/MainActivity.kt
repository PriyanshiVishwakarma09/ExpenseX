package com.example.expensex

import android.Manifest // ✅ Required for POST_NOTIFICATIONS
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.expensex.navigation.Navigation
import com.example.expensex.ui.theme.ExpenseXTheme
import com.example.expensex.worker.BudgetWorker
import com.example.expensex.worker.startBudgetWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // This creates the popup that asks for notification permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            android.util.Log.d("Permission", "Notification permission granted!")
        } else {
            android.util.Log.e("Permission", "Notification permission denied!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        // This actually triggers the popup when the app opens (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        WorkManager.getInstance(this).cancelAllWork()
        val request = OneTimeWorkRequestBuilder<BudgetWorker>().build()
        WorkManager.getInstance(this).enqueue(request)
        startBudgetWorker(this)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ExpenseXTheme {
                Navigation()
            }
        }
    }
}