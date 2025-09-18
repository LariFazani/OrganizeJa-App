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

        fun bind(meta: Meta) {
            binding.nomeMetaTextView.text = meta.nome

            val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val valorEconomizado = meta.valorEconomizado
            val valorTotal = meta.valorTotal

            binding.valorEconomizadoMetaTextView.text = "${formatadorMoeda.format(valorEconomizado)}"
            binding.valorTotalMetaTextView.text = "${formatadorMoeda.format(valorTotal)}"

            val progresso = if (valorTotal > 0) (valorEconomizado / valorTotal * 100).toInt() else 0
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

    fun updateMetas(newMetas: List<Meta>) {
        metaList = newMetas
        notifyDataSetChanged()
    }
}