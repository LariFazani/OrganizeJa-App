package com.example.organizeja.ui.transacoes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.organizeja.R
import com.example.organizeja.databinding.FragmentAdicionarTransacaoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.NumberFormat


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

        setupCategoriaSpinner()

        // Remove o clique do EditText e adiciona o clique ao ícone de calendário
        binding.dataInputLayout.setEndIconOnClickListener {
            showDatePicker()
        }

        // Adiciona o TextWatcher para a formatação da data
        setupDateTextWatcher()

        setupValorEditText()

        // Lida com o clique do botão de salvar
        binding.buttonSalvar.setOnClickListener {
            // Chama a função de validação e salvamento
            salvarTransacao()
        }

        // Lida com o clique do botão de cancelar
        binding.buttonCancelar.setOnClickListener {
            // Retorna para a tela anterior
            findNavController().popBackStack()
        }
    }

    private fun setupCategoriaSpinner() {
        val categorias = resources.getStringArray(R.array.categorias_array)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapter
    }

    private fun setupDateTextWatcher() {
        binding.dataEditText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var isDeleting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || isDeleting) {
                    return
                }

                isFormatting = true

                val text = s.toString().replace("/", "")

                if (text.length > 8) {
                    s?.delete(8, text.length)
                    isFormatting = false
                    return
                }

                val formatted = StringBuilder()
                formatted.append(text)

                if (formatted.length >= 2) {
                    formatted.insert(2, "/")
                }
                if (formatted.length >= 5) {
                    formatted.insert(5, "/")
                }

                val finalFormatted = formatted.toString()
                if (finalFormatted != s.toString()) {
                    val selection = binding.dataEditText.selectionStart
                    binding.dataEditText.setText(finalFormatted)
                    binding.dataEditText.setSelection(selection + (finalFormatted.length - s.toString().length))
                }

                isFormatting = false
            }
        })
    }

    // Altere a função setupDatePicker para showDatePicker e remova a linha de setOnClickListener
    private fun showDatePicker() {
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

    private fun setupValorEditText() {
        val locale = Locale("pt", "BR") // Usa a localização para o Brasil (Real)
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)

        // Adicione um TextWatcher ao EditText de valor
        binding.valorEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar aqui
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Não é necessário implementar aqui
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    binding.valorEditText.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[R$,.\\s]".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble() / 100
                        val formatted = currencyFormatter.format(parsed)

                        current = formatted
                        binding.valorEditText.setText(formatted)
                        binding.valorEditText.setSelection(formatted.length)
                    } else {
                        current = ""
                        binding.valorEditText.setText("")
                    }

                    binding.valorEditText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun salvarTransacao() {
        val descricao = binding.descricaoEditText.text.toString().trim()
        val valorStr = binding.valorEditText.text.toString().trim()
        val dataStr = binding.dataEditText.text.toString().trim()
        val categoriaSelecionada = binding.spinnerCategoria.selectedItemPosition

        // Verifica se a descrição, o valor e o tipo de transação foram preenchidos.
        val tipoSelecionado = binding.toggleButtonGroupTipo.checkedButtonId != -1

        // Validação principal: todos os campos obrigatórios
        if (descricao.isEmpty() || valorStr.isEmpty() || !tipoSelecionado || categoriaSelecionada == 0) {
            Toast.makeText(context, "Por favor, preencha todos os campos e selecione uma categoria.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validação da data
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            dateFormat.isLenient = false // Garante que a data seja estrita
            dateFormat.parse(dataStr)

            // Se a validação da data passar, o código continua
            Toast.makeText(context, "Transação salva com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()

        } catch (e: Exception) {
            // Se a data for inválida
            Toast.makeText(context, "Por favor, insira uma data completa.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}