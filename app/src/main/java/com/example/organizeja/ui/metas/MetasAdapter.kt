package com.example.organizeja.ui.metas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organizeja.databinding.ItemMetasBinding
import com.example.organizeja.model.Meta
import java.text.NumberFormat
import java.util.Locale

class MetasAdapter(
    private var metaList: List<Meta>
) : RecyclerView.Adapter<MetasAdapter.MetaViewHolder>() {

    class MetaViewHolder(private val binding: ItemMetasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Função que vincula os dados da meta com os elementos de UI
        fun bind(meta: Meta) {
            binding.nomeMetaTextView.text = meta.nome

            // Formata o valor economizado e o valor total para exibição
            val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val valorEconomizado = meta.valorEconomizado // Assume que a Meta terá essa propriedade
            val valorTotal = meta.valorTotal // Assume que o valor total é a propriedade 'valor'


            binding.valorEconomizadoMetaTextView.text = "${formatadorMoeda.format(valorEconomizado)}"

            binding.valorTotalMetaTextView.text = "${formatadorMoeda.format(valorTotal)}"

            // Calcula o progresso e atualiza a barra de progresso
            val progresso = (valorEconomizado / valorTotal * 100).toInt()
            binding.progressoMetaProgressBar.progress = progresso
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetaViewHolder {
        val binding = ItemMetasBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MetaViewHolder, position: Int) {
        holder.bind(metaList[position])
    }

    override fun getItemCount(): Int = metaList.size

    // Função para atualizar a lista de metas
    fun updateMetas(newMetas: List<Meta>) {
        metaList = newMetas
        notifyDataSetChanged()
    }
}