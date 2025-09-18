package com.example.organizeja.ui.metas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organizeja.R
import com.example.organizeja.databinding.FragmentMetasBinding
import androidx.fragment.app.activityViewModels // Importação necessária

class MetasFragment : Fragment() {

    private var _binding: FragmentMetasBinding? = null
    private val binding get() = _binding!!
    private lateinit var metasAdapter: MetasAdapter

    // Obtém a mesma instância do ViewModel usada por outros fragments na mesma atividade
    private val metasViewModel: MetasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMetasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        metasAdapter = MetasAdapter(emptyList())

        binding.metasRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = metasAdapter
        }

        // Observa a lista de metas do ViewModel
        metasViewModel.metas.observe(viewLifecycleOwner) { metasList ->
            if (metasList.isEmpty()) {
                binding.metasRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                binding.metasRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE
                metasAdapter.updateMetas(metasList)
            }
        }

        binding.addMetaButton.setOnClickListener {
            findNavController().navigate(R.id.action_metas_to_adicionarMeta)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}