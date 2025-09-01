package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizeja.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout usando View Binding
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Oculta a barra de título (ActionBar/Toolbar)
        supportActionBar?.hide()

        // Lógica para o botão "Prosseguir"
        binding.prosseguirButton.setOnClickListener {
            // Aqui você pode adicionar a lógica de validação dos campose o processamento do cadastro.
            val email = binding.emailEditText.text.toString()
            val cpf = binding.cpfEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validação dos campos
            if (email.isEmpty() || cpf.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validação de formato de E-mail
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido", Toast.LENGTH_SHORT).show()
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            // Validação de CPF completo (com 11 dígitos numéricos ou 14 caracteres formatados)
            val cleanCpf = cpf.replace("[^\\d]".toRegex(), "") // Remove a formatação para validar
            if (cleanCpf.length != 11) {
                Toast.makeText(this, "Por favor, preencha o CPF completo", Toast.LENGTH_SHORT).show()
                binding.cpfEditText.requestFocus()
                return@setOnClickListener
            }

            // Validação de senhas
            if (password.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 dígitos", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }
            if (!password.contains(Regex(".*\\d.*"))) { // Verifica se contém pelo menos um número
                Toast.makeText(this, "A senha deve conter pelo menos 1 número", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }
            if (!password.contains(Regex(".*[A-Z].*"))) { // Verifica se contém pelo menos uma letra maiúscula
                Toast.makeText(this, "A senha deve conter pelo menos 1 letra maiúscula", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }
            // Regex para caracteres especiais: ~!@#$%^&*_-+=`|\(){}[]:;"'<>,.?/
            val specialCharRegex = Regex(".*[~!@#$%^&*\\-_+=`|\\(\\)\\{\\}\\[\\]:;\"'<>,.?/].*")
            if (!password.contains(specialCharRegex)) {
                Toast.makeText(this, "A senha deve conter pelo menos 1 caractere especial", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Se todas as validações passarem, prosseguir para o login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Lógica para o link "login"
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Formatação de CPF (Exemplo simples: adiciona ponto e hífen automaticamente)
        binding.cpfEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    binding.cpfEditText.removeTextChangedListener(this)
                    val cleanString = s.toString().replace("[^\\d]".toRegex(), "")
                    val sb = StringBuilder(cleanString)

                    if (sb.length > 3) sb.insert(3, '.')
                    if (sb.length > 7) sb.insert(7, '.')
                    if (sb.length > 11) sb.insert(11, '-')

                    current = sb.toString()
                    binding.cpfEditText.setText(current)
                    binding.cpfEditText.setSelection(current.length)
                    binding.cpfEditText.addTextChangedListener(this)
                }
            }
        })
    }
}