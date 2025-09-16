package com.example.organizeja.ui.metas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizeja.model.Meta

class MetasViewModel : ViewModel() {

    // Lista de metas
    private val _meta = MutableLiveData<List<Meta>>()
    val meta: LiveData<List<Meta>> = _meta

    init {
        // Carrega dados de exemplo
        loadSampleMetas()
    }

    private fun loadSampleMetas() {
        val sampleList = listOf(
            Meta(
                id = "1",
                nome = "Viagem para a Europa",
                valorTotal = 20000.00,
                valorEconomizado = 3500.00
            ),
            Meta(
                id = "2",
                nome = "Comprar um Carro",
                valorTotal = 50000.00,
                valorEconomizado = 15000.00
            ),
            Meta(
                id = "3",
                nome = "Fazer um curso",
                valorTotal = 2500.00,
                valorEconomizado = 1200.00
            ),
            Meta(
                id = "4",
                nome = "Presente de Aniversário",
                valorTotal = 500.00,
                valorEconomizado = 50.00
            )
        )
        _meta.value = sampleList
    }
}
