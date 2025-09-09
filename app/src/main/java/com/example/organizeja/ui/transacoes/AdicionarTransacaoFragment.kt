package com.example.organizeja.ui.transacoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.organizeja.R
import com.example.organizeja.databinding.FragmentAdicionarTransacaoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ArrayAdapter

class AdicionarTransacaoFragment : Fragment() {

    private var _binding: FragmentAdicionarTransacaoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdicionarTransacaoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o Spinner com as categorias
        setupCategoriaSpinner()

        // Configura o DatePicker
        setupDatePicker()

        // Lida com o clique do botão de salvar
        binding.buttonSalvar.setOnClickListener {
            // Lógica para salvar a transação
            // Você pode adicionar a validação dos campos aqui
            Toast.makeText(context, "Salvar Clicado!", Toast.LENGTH_SHORT).show()
        }

        // Lida com o clique do botão de cancelar
        binding.buttonCancelar.setOnClickListener {
            // Lógica para cancelar e fechar a tela
            // Exemplo: navigation.findNavController().popBackStack()
            Toast.makeText(context, "Cancelar Clicado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCategoriaSpinner() {
        val categorias = resources.getStringArray(R.array.categorias_array)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.dataEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data da transação")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val date = Date(selection)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                binding.dataEditText.setText(dateFormat.format(date))
            }

            datePicker.show(requireFragmentManager(), "DatePicker")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}