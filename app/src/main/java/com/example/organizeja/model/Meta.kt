package com.example.organizeja.model

data class Meta(
    // ID da transação (usado principalmente para o Firestore)
    // Pode ser gerado pelo próprio Firestore ao adicionar o documento
    var id: String = "",

    // Nome da Meta
    var nome: String = "",

    // Valor total da meta
    var valorTotal: Double = 0.0,

    // Valor economizado até o momento
    var valorEconomizado: Double = 0.0

)