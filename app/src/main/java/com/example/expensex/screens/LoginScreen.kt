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
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.expensex.SessionManager
import com.example.expensex.model.Routes
import com.example.expensex.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    vm : AuthViewModel,
    navController: NavController,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column {

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {


            Text("Expense X", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(email, { email = it }, label = { Text("Email") })
            OutlinedTextField(password, { password = it }, label = { Text("Password") })

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.login(email, password) { ok, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (ok) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            SessionManager(context).saveUid(uid)

                            navController.navigate("${Routes.HOME}/$uid") {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                }
            ) {
                Text("Login")
            }

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//
//
//    val vm = AuthViewModel()
//    LoginScreen(
//        vm,
//        onNavigateToRegister = {},
//        onLoginSuccess = {}
//    )
//}



