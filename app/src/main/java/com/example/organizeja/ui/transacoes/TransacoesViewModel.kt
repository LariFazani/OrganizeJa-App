package com.example.organizeja.ui.transacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizeja.model.Transacao
import java.util.Date

class TransacoesViewModel : ViewModel() {

    // Lista de transações (exemplo)
    private val _transactions = MutableLiveData<List<Transacao>>()
    val transactions: LiveData<List<Transacao>> = _transactions

    init {
        // Carrega dados de exemplo
        loadSampleTransactions()
    }

    private fun loadSampleTransactions() {
        val sampleList = listOf(
            Transacao(
                id = "1",
                descricao = "Salário",
                valor = 3500.00,
                tipo = "Receita",
                categoria = "Salário",
                data = Date()
            ),
            Transacao(
                id = "2",
                descricao = "Supermercado",
                valor = 150.75,
                tipo = "Despesa",
                categoria = "Alimentação",
                data = Date()
            ),
            Transacao(
                id = "3",
                descricao = "Conta de Luz",
                valor = 250.00,
                tipo = "Despesa",
                categoria = "Contas",
                data = Date()
            ),
            Transacao(
                id = "4",
                descricao = "Freelance",
                valor = 500.00,
                tipo = "Receita",
                categoria = "Serviços",
                data = Date()
            )
        )
        _transactions.value = sampleList
    }

    // Você pode adicionar uma função aqui para buscar os dados reais do Firestore
    // fun fetchTransactionsFromFirestore() { ... }
}
