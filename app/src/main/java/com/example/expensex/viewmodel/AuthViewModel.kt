package com.example.expensex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expensex.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.SessionManager
import com.example.expensex.repository.WalletRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo : AuthRepository,
    private val repository: WalletRepository ,
    private val sessionManager: SessionManager
) : ViewModel(){

    var loading by mutableStateOf(false)
        private set

    fun register(name :String , email : String , pass : String , onDone : (Boolean , String) -> Unit){
        loading = true
        repo.register(name , email , pass){ ok , msg ->
            loading = false
            if(ok){
                prepareUserSession()
            }
            onDone(ok , msg)
        }
    }
    fun login(email: String , pass: String , OnDone : (Boolean, String) -> Unit){
        loading = true
        repo.login(email , pass){ ok , msg ->
            loading = false
            if(ok){
                prepareUserSession()
            }
            OnDone(ok , msg)
        }
    }
    fun prepareUserSession(){
        viewModelScope.launch {
            val uid = repo.getCurrentUser()?.uid ?: return@launch
            sessionManager.saveUid(uid)
            repository.ensureMainAccount(uid)
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