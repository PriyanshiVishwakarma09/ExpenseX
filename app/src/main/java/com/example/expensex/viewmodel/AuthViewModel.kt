package com.example.expensex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expensex.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo : AuthRepository
) : ViewModel(){

    var loading by mutableStateOf(false)
        private set

    fun register(name :String , email : String , pass : String , onDone : (Boolean , String) -> Unit){
        loading = true
        repo.register(name , email , pass){ ok , msg ->
            loading = false
            onDone(ok , msg)
        }
    }
    fun login(email: String , pass: String , OnDone : (Boolean, String) -> Unit){
        loading = true
        repo.login(email , pass){ ok , msg ->
            loading = false
            OnDone(ok , msg)
        }
    }

}

    // UI
    // ↓ calls
    // ViewModel.login()
    // ↓ sets loading = true
    // Repository.login()
    // ↓ Firebase Auth
    // Callback returns (ok, msg)
    // ↓ loading = false
    // UI gets result via OnDone()