package com.example.organizeja.ui.configuracao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.organizeja.databinding.FragmentConfiguracaoBinding

class ConfiguracaoFragment : Fragment() {

    private var _binding: FragmentConfiguracaoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfiguracaoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o ouvinte de clique para o botão "Sair"
        binding.botaoSair.setOnClickListener {
            // Lógica para deslogar o usuário
            // Exemplo: deslogar do Firebase, limpar dados de sessão, e navegar para a tela de login
            Toast.makeText(context, "Deslogando...", Toast.LENGTH_SHORT).show()
        }

        // Configura o ouvinte de clique para a opção "Notificação e alertas"
        binding.notificacaoTextView.setOnClickListener {
            // Lógica para navegar para a tela de notificação e alertas
            Toast.makeText(context, "Notificação e alertas clicado!", Toast.LENGTH_SHORT).show()
        }

        // Configura o ouvinte de clique para a opção "Mudar senha"
        binding.mudarSenhaTextView.setOnClickListener {
            // Lógica para navegar para a tela de mudança de senha
            Toast.makeText(context, "Mudar senha clicado!", Toast.LENGTH_SHORT).show()
        }

        // Configura o ouvinte de clique para a opção "Excluir conta"
        binding.excluirContaTextView.setOnClickListener {
            // Lógica para navegar para a tela de excluir conta
            Toast.makeText(context, "Excluir conta clicado!", Toast.LENGTH_SHORT).show()
        }

        // Configura o ouvinte de clique para a opção "Sobre"
        binding.sobreTextView.setOnClickListener {
            // Lógica para navegar para a tela "Sobre"
            Toast.makeText(context, "Sobre clicado!", Toast.LENGTH_SHORT).show()
        }

        // Adicionar lógica para carregar o nome e e-mail do usuário
        // Por exemplo:
        // binding.nameTextView.text = "Nome do Usuário Logado"
        // binding.emailTextView.text = "email.do.usuario@app.com"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}