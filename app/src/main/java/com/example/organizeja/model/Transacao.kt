package com.example.organizeja.model

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class Transacao(
    var id: String = "",

    @get:PropertyName("description") @set:PropertyName("description")
    var descricao: String = "",

    @get:PropertyName("amount") @set:PropertyName("amount")
    var valor: Double = 0.0,

    @get:PropertyName("type") @set:PropertyName("type")
    var tipo: String = "",

    @get:PropertyName("category") @set:PropertyName("category")
    var categoria: String = "",

    @ServerTimestamp
    @get:PropertyName("date") @set:PropertyName("date")
    var data: Date? = null,
    @get:PropertyName("userId") @set:PropertyName("userId")
    var userId: String = ""
)