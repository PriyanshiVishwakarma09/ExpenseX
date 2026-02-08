package com.example.expensex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensex.repository.WalletRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class walletViewModel(
    private val repo : WalletRepository
) : ViewModel() {

    var balance by mutableStateOf(0.0)
        private set  //setter

    private var accountId : Int ? = null


    private val uid =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun load(){
        viewModelScope.launch {
            val acc= repo.getMainAccount(uid)
            if(acc != null){
                balance = acc.balance
                accountId = acc.id
            }
        }
    }

    fun addIncome(title : String , amount : Double){
        val id = accountId ?: return
        viewModelScope.launch {
            repo.addIncome(uid, title , amount , id)
            balance += amount
        }
    }

}