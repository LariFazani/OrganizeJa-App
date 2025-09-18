package com.example.organizeja.ui.metas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizeja.model.Meta
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class MetasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val metasCollection = db.collection("goals")

    private val _metas = MutableLiveData<List<Meta>>()
    val metas: LiveData<List<Meta>> = _metas

    // Use o ID de usuário do seu sistema de autenticação. Este é um exemplo.
    private val userId = "Exemplo_user_id"

    private var metasListener: ListenerRegistration? = null

    init {
        // Conecta ao Firestore para observar as metas em tempo real
        metasListener = metasCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Trate os erros aqui, se necessário
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val metasList = snapshot.documents.mapNotNull { document ->
                        val meta = document.toObject(Meta::class.java)
                        meta?.id = document.id
                        meta
                    }
                    _metas.value = metasList
                }
            }
    }

    // Função para adicionar uma nova meta ao Firestore
    fun addMeta(meta: Meta) {
        viewModelScope.launch {
            metasCollection.add(meta)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove o ouvinte para evitar vazamentos de memória
        metasListener?.remove()
    }
}