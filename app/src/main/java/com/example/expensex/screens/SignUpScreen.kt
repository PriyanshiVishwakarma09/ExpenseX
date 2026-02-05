package com.example.expensex.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.viewmodel.AuthViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton

@Composable
    fun RegisterScreen(
    vm: AuthViewModel,
    onNavigateToLogin: () -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(name, { name = it }, label = { Text("Name") })
            OutlinedTextField(email, { email = it }, label = { Text("Email") })
            OutlinedTextField(password, { password = it }, label = { Text("Password") })

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.register(name, email, password) { ok, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (ok) onNavigateToLogin()
                    }
                }
            ) {
                Text("Sign Up")
            }

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }

