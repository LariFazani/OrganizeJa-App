package com.example.organizeja.model

import com.google.firebase.firestore.PropertyName

data class Meta(
    var id: String = "",

    @get:PropertyName("goalName") @set:PropertyName("goalName")
    var nome: String = "",

    @get:PropertyName("targetAmount") @set:PropertyName("targetAmount")
    var valorTotal: Double = 0.0,

    @get:PropertyName("savedAmount") @set:PropertyName("savedAmount") // Removido o espaço
    var valorEconomizado: Double = 0.0,

    @get:PropertyName("userId") @set:PropertyName("userId")
    var userId: String = "" // Adicionado o campo userId
)