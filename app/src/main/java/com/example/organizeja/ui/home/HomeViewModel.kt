package com.example.organizeja.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // MutableLiveData para o saldo, que pode ser alterado internamente no ViewModel
    private val _balanceAmount = MutableLiveData<String>().apply {
        // Define um valor inicial para o saldo
        value = "$3.469,52"
    }

    // LiveData pública para o saldo, que pode ser observada pelo Fragment (apenas leitura)
    val balanceAmount: LiveData<String> = _balanceAmount

    // Você pode adicionar funções aqui para atualizar o saldo, buscar dados, etc.
    fun updateBalance(newBalance: String) {
        _balanceAmount.value = newBalance
    }
}