package com.example.expensex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensex.db.ExpenseDatabase
import com.example.expensex.navigation.Navigation
import com.example.expensex.repository.WalletRepository
import com.example.expensex.screens.WalletScreen
import com.example.expensex.ui.theme.ExpenseXTheme
import com.example.expensex.viewmodel.walletViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseXTheme {
               Navigation()
            }
        }
    }
}
