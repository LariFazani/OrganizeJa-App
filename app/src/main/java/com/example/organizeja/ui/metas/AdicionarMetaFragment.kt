package com.example.organizeja.ui.adicionarmeta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.organizeja.databinding.FragmentAdicionarMetaBinding
import com.example.organizeja.ui.metas.MetasViewModel
import androidx.fragment.app.activityViewModels
import com.example.organizeja.model.Meta
import java.text.NumberFormat
import java.util.Locale

class AdicionarMetaFragment : Fragment() {

    private var _binding: FragmentAdicionarMetaBinding? = null
    private val binding get() = _binding!!
    private val metasViewModel: MetasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdicionarMetaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValorEditText(binding.valorTotalEditText)
        setupValorEditText(binding.valorEconomizadoEditText)

        binding.buttonSalvar.setOnClickListener {
            salvarMeta()
        }

        binding.buttonCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupValorEditText(editText: com.google.android.material.textfield.TextInputEditText) {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        editText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)
                    val cleanString = s.toString().replace("[R$,.\\s]".toRegex(), "")
                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble() / 100
                        val formatted = currencyFormatter.format(parsed)
                        current = formatted
                        editText.setText(formatted)
                        editText.setSelection(formatted.length)
                    } else {
                        current = ""
                        editText.setText("")
                    }
                    editText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun salvarMeta() {
        val nomeMeta = binding.nomeMetaEditText.text.toString().trim()
        val valorTotalStr = binding.valorTotalEditText.text.toString().trim()
        val valorEconomizadoStr = binding.valorEconomizadoEditText.text.toString().trim()

        if (nomeMeta.isEmpty() || valorTotalStr.isEmpty() || valorEconomizadoStr.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val valorTotal = valorTotalStr.replace("[R$,.\\s]".toRegex(), "").toDouble() / 100
        val valorEconomizado = valorEconomizadoStr.replace("[R$,.\\s]".toRegex(), "").toDouble() / 100

        // Você precisa obter o ID de usuário do seu sistema de autenticação,
        // mas usaremos um valor de exemplo por enquanto.
        val novaMeta = Meta(
            nome = nomeMeta,
            valorTotal = valorTotal,
            valorEconomizado = valorEconomizado,
            userId = "Exemplo_user_id"
        )

        metasViewModel.addMeta(novaMeta)

        Toast.makeText(context, "Meta salva com sucesso!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}