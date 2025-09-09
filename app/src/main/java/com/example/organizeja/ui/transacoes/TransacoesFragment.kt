package com.example.organizeja.ui.transacoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organizeja.R
import com.example.organizeja.databinding.FragmentTransacoesBinding

class TransacoesFragment : Fragment() {

    private var _binding: FragmentTransacoesBinding? = null
    private val binding get() = _binding!!
    private lateinit var transacoesAdapter: TransacoesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transacoesViewModel =
            ViewModelProvider(this).get(TransacoesViewModel::class.java)

        _binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializa o adaptador com uma lista vazia.
        transacoesAdapter = TransacoesAdapter(emptyList())

        // Configura o RecyclerView
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transacoesAdapter
        }

        // Observa a lista de transações do ViewModel e atualiza o RecyclerView
        transacoesViewModel.transactions.observe(viewLifecycleOwner) { transactionsList ->
            if (transactionsList.isEmpty()) {
                // Se a lista estiver vazia, mostra a mensagem e esconde o RecyclerView
                binding.transactionsRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                // Se houver itens, mostra o RecyclerView e esconde a mensagem
                binding.transactionsRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE
                transacoesAdapter.updateTransactions(transactionsList)
            }
        }

        // Adiciona um listener de clique ao novo botão de ação flutuante (FAB)
        binding.addButton.setOnClickListener {
            // Navega para a tela de adicionar transação
            findNavController().navigate(R.id.action_transacoes_to_adicionarTransacao)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}