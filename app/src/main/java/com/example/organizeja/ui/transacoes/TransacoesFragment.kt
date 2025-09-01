package com.example.organizeja.ui.transacoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.organizeja.databinding.FragmentTransacoesBinding

class TransacoesFragment : Fragment() {

    //Referência para a classe de binding gerada automaticamente para o layout fragment_transacoes.xml
    // A view binding é uma forma segura de acessar as views do layout
    private var _binding: FragmentTransacoesBinding? = null

    private val binding get() = _binding!!

    //Criar interface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val TransacoesViewModel =
            ViewModelProvider(this).get(TransacoesViewModel::class.java)

        _binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTransacoes
        TransacoesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}