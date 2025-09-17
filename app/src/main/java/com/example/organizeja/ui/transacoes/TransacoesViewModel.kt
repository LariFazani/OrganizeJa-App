// TransacoesViewModel.kt
package com.example.organizeja.ui.transacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizeja.model.Transacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class TransacoesViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transacao>>()
    val transactions: LiveData<List<Transacao>> = _transactions

    private var firestoreListener: ListenerRegistration? = null

    init {
        listenForTransactions()
    }

    // Altere a visibilidade da função de 'private' para pública (removendo a palavra-chave).
    // O Kotlin a torna pública por padrão.
    fun listenForTransactions() {
        if (firestoreListener != null) {
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val db = FirebaseFirestore.getInstance()

            val query = db.collection("transactions")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)

            firestoreListener = query.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val transactionsList = mutableListOf<Transacao>()
                    for (doc in snapshots.documents) {
                        val transacao = doc.toObject(Transacao::class.java)
                        if (transacao != null) {
                            transacao.id = doc.id
                            transactionsList.add(transacao)
                        }
                    }
                    _transactions.value = transactionsList
                }
            }
        }
    }

    fun deleteTransaction(transacaoId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("transactions").document(transacaoId)
            .delete()
            .addOnSuccessListener {
                val currentList = _transactions.value.orEmpty().toMutableList()
                val updatedList = currentList.filter { it.id != transacaoId }
                _transactions.value = updatedList
            }
            .addOnFailureListener { e ->
                // Lidar com o erro
            }
    }

    override fun onCleared() {
        super.onCleared()
        firestoreListener?.remove()
    }
}