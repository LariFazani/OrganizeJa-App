package com.example.organizeja.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.organizeja.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // Esta propriedade é válida apenas entre onCreateView e onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicializa o HomeViewModel
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // Infla o layout usando View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observa mudanças no saldo do ViewModel e atualiza a UI
        homeViewModel.balanceAmount.observe(viewLifecycleOwner) {
            binding.balanceAmountTextView.text = it
        }

        // Exemplo de como lidar com o clique no ícone de visibilidade (opcional)
        // Você pode implementar a lógica para ocultar/mostrar o saldo aqui.
        binding.visibilityIcon.setOnClickListener {
            // Lógica para alternar a visibilidade do saldo
            // Por exemplo:
            // if (binding.balanceAmountTextView.visibility == View.VISIBLE) {
            //     binding.balanceAmountTextView.visibility = View.INVISIBLE
            //     binding.visibilityIcon.setImageResource(R.drawable.ic_visibility_off_black_24dp)
            // } else {
            //     binding.balanceAmountTextView.visibility = View.VISIBLE
            //     binding.visibilityIcon.setImageResource(R.drawable.ic_visibility_black_24dp)
            // }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
