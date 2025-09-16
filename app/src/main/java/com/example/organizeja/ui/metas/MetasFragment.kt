package com.example.organizeja.ui.metas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organizeja.R
import com.example.organizeja.databinding.FragmentMetasBinding

class MetasFragment : Fragment() {

    private var _binding: FragmentMetasBinding? = null
    private val binding get() = _binding!!
    private lateinit var metasAdapter: MetasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val metasViewModel = ViewModelProvider(this).get(MetasViewModel::class.java)

        _binding = FragmentMetasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializa o adaptador com uma lista vazia
        metasAdapter = MetasAdapter(emptyList())

        // Configura o RecyclerView
        binding.metasRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = metasAdapter
        }

        // Observa a lista de metas do ViewModel e atualiza o RecyclerView
        metasViewModel.meta.observe(viewLifecycleOwner) { metasList ->
            if (metasList.isEmpty()) {
                // Se a lista estiver vazia, mostra a mensagem e esconde o RecyclerView
                binding.metasRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                // Se houver itens, mostra o RecyclerView e esconde a mensagem
                binding.metasRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE
                metasAdapter.updateMetas(metasList)
            }
        }

        // Adiciona um listener de clique ao novo botão de ação flutuante (FAB)
        binding.addMetaButton.setOnClickListener {
            // Navega para a tela de adicionar transação
            findNavController().navigate(R.id.action_metas_to_adicionarMeta)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
