// TransacoesFragment.kt
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
import com.example.organizeja.model.Transacao
import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener

class TransacoesFragment : Fragment(), OnTransacaoDeleteListener {

    private var _binding: FragmentTransacoesBinding? = null
    private val binding get() = _binding!!
    private lateinit var transacoesAdapter: TransacoesAdapter
    private lateinit var transacoesViewModel: TransacoesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transacoesViewModel = ViewModelProvider(this).get(TransacoesViewModel::class.java)

        // Inicializa o adaptador e o RecyclerView
        transacoesAdapter = TransacoesAdapter(emptyList(), this)
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transacoesAdapter
        }

        // Observa a lista de transações do ViewModel
        transacoesViewModel.transactions.observe(viewLifecycleOwner) { transactionsList ->
            if (transactionsList.isEmpty()) {
                binding.transactionsRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                binding.transactionsRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE
                transacoesAdapter.updateTransactions(transactionsList)
            }
        }

        // Remove a chamada do listener daqui, pois o onViewCreated é executado apenas uma vez.
        // A chamada será feita no onStart() para garantir a atualização.

        binding.addTransacoesButton.setOnClickListener {
            findNavController().navigate(R.id.action_transacoes_to_adicionarTransacao)
        }
    }

    override fun onStart() {
        super.onStart()
        // O listener do Firestore é ativado aqui, garantindo que a lista seja
        // atualizada sempre que o fragmento se tornar visível.
        transacoesViewModel.listenForTransactions()
    }

    override fun onDeleteClick(transacao: Transacao) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Transação")
            .setMessage("Tem certeza que deseja excluir esta transação?")
            .setPositiveButton("Sim") { _, _ ->
                transacoesViewModel.deleteTransaction(transacao.id)
                Toast.makeText(context, "Transação excluída!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}