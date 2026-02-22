package com.example.expensex.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensex.SessionManager
import com.example.expensex.model.Routes
import com.example.expensex.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen(
    vm: AuthViewModel,
    navController: NavController,
    onNavigateToRegister: () -> Unit
) {
    val TealColor = Color(0xFF3B978F)
    val BgLightColor = Color(0xFFF8F9FA)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(containerColor = BgLightColor) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(TealColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Welcome Back",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Login to manage your expenses",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(40.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TealColor) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealColor,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TealColor) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealColor,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                // Your exact business logic preserved here
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
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealColor)
                        ) {
                            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(300.dp))

                TextButton(onClick = onNavigateToRegister) {
                    Text("Don't have an account? Sign Up", color = TealColor, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}