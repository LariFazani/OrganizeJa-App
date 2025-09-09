package com.example.organizeja.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Transacao(
    // ID da transação (usado principalmente para o Firestore)
    // Pode ser gerado pelo próprio Firestore ao adicionar o documento
    var id: String = "",

    // Descrição da transação (ex: "Compras no mercado", "Salário")
    var descricao: String = "",

    // Valor da transação. Usar Double para valores monetários é comum.
    var valor: Double = 0.0,

    // Tipo de transação: "Receita" ou "Despesa"
    var tipo: String = "",

    // Campo opcional para categorizar a transação (ex: "Alimentação", "Transporte")
    var categoria: String = "",
    // Carimbo de data/hora do servidor do Firestore.
    // Isso garante que a data seja a do momento em que o documento foi criado no banco de dados.
    // É uma boa prática para evitar problemas de fuso horário.
    @ServerTimestamp
    var data: Date? = null
)