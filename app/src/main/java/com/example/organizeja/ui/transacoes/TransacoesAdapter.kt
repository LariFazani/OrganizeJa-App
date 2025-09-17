// TransacoesAdapter.kt
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

// Define a interface para o clique do botão
interface OnTransacaoDeleteListener {
    fun onDeleteClick(transacao: Transacao)
}

class TransacoesAdapter(
    private var transacoes: List<Transacao>,
    private val deleteListener: OnTransacaoDeleteListener
) : RecyclerView.Adapter<TransacoesAdapter.TransacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val binding = ItemTransacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransacaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = transacoes[position]
        holder.bind(transacao, deleteListener)
    }

    override fun getItemCount(): Int = transacoes.size

    fun updateTransactions(newTransactions: List<Transacao>) {
        this.transacoes = newTransactions
        notifyDataSetChanged()
    }

    class TransacaoViewHolder(private val binding: ItemTransacaoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transacao: Transacao, deleteListener: OnTransacaoDeleteListener) {
            binding.textCategory.text = transacao.categoria
            binding.textTransactionName.text = transacao.descricao

            transacao.data?.let {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                binding.textDate.text = dateFormat.format(it)
            }

            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val valorFormatado = numberFormat.format(transacao.valor)

            if (transacao.tipo == "Receita") {
                binding.textAmount.setTextColor(Color.parseColor("#4CAF50"))
                binding.textAmount.text = "+ $valorFormatado"
            } else {
                binding.textAmount.setTextColor(Color.parseColor("#F44336"))
                binding.textAmount.text = "- $valorFormatado"
            }

            // Define o clique do botão de exclusão
            binding.buttonDelete.setOnClickListener {
                deleteListener.onDeleteClick(transacao)
            }
        }
    }
}