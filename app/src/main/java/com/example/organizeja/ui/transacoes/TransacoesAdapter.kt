package com.example.organizeja.ui.transacoes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organizeja.databinding.ItemTransacaoBinding
import com.example.organizeja.model.Transacao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// O adapter é responsável por vincular os dados da lista Transacao aos itens da RecyclerView.
class TransacoesAdapter(private var transacoes: List<Transacao>) :
    RecyclerView.Adapter<TransacoesAdapter.TransacaoViewHolder>() {

    // Cria e retorna um ViewHolder para o layout do item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val binding = ItemTransacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransacaoViewHolder(binding)
    }

    // Vincula os dados a uma ViewHolder específica.
    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = transacoes[position]
        holder.bind(transacao)
    }

    // Retorna o número total de itens na lista.
    override fun getItemCount(): Int = transacoes.size

    // ViewHolder para o item da transação.
    class TransacaoViewHolder(private val binding: ItemTransacaoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transacao: Transacao) {
            // Define a categoria e o nome da transação.
            binding.textCategory.text = transacao.categoria
            binding.textTransactionName.text = transacao.descricao

            // Formata a data se ela não for nula.
            transacao.data?.let {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                binding.textDate.text = dateFormat.format(it)
            }

            // Formata e define o valor, alterando a cor com base no tipo.
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val valorFormatado = numberFormat.format(transacao.valor)
            binding.textAmount.text = valorFormatado

            val color = if (transacao.tipo == "Receita") {
                Color.parseColor("#4CAF50") // Verde
            } else {
                Color.parseColor("#F44336") // Vermelho
            }
            binding.textAmount.setTextColor(color)
        }
    }

    // Adiciona uma nova função para atualizar a lista de transações
    fun updateTransactions(newTransactions: List<Transacao>) {
        this.transacoes = newTransactions
        notifyDataSetChanged()
    }
}
