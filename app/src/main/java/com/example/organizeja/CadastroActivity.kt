package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizeja.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout usando View Binding
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o Firebase Auth e Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Oculta a barra de título (ActionBar/Toolbar)
        supportActionBar?.hide()

        // Lógica para o botão "Prosseguir"
        binding.prosseguirButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val cpf = binding.cpfEditText.text.toString().replace("[^\\d]".toRegex(), "")
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validação dos campos
            if (email.isEmpty() || cpf.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!validarCpf(cpf)) {
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "A senha deve ter pelo menos 8 caracteres, um dígito e um caractere especial", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Inicia o processo de cadastro no Firebase Auth
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Cadastro no Firebase Auth bem-sucedido, agora salva os dados no Firestore
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            salvarDadosUsuario(user, email, cpf)
                        }
                    } else {
                        // Cadastro falhou
                        Toast.makeText(this, "Falha no cadastro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
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

    // Função para salvar os dados do usuário no Firestore
    private fun salvarDadosUsuario(user: FirebaseUser, email: String, cpf: String) {
        val userData = hashMapOf(
            "email" to email,
            "cpf" to cpf
        )
        firebaseFirestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastro e dados salvos com sucesso!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar dados do usuário: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun validarCpf(cpf: String): Boolean {
        if (cpf.length != 11) return false
        val cpfNumeros = cpf.filter { it.isDigit() }.map { it.toString().toInt() }
        if (cpfNumeros.size != 11) return false

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpfNumeros.all { it == cpfNumeros[0] }) return false

        // Validação do primeiro dígito verificador
        var sum = 0
        for (i in 0..8) {
            sum += cpfNumeros[i] * (10 - i)
        }
        var resto = 11 - (sum % 11)
        if (resto > 9) resto = 0
        if (resto != cpfNumeros[9]) return false

        // Validação do segundo dígito verificador
        sum = 0
        for (i in 0..9) {
            sum += cpfNumeros[i] * (11 - i)
        }
        resto = 11 - (sum % 11)
        if (resto > 9) resto = 0

        return resto == cpfNumeros[10]
    }

    // Função de validação de senha
    private fun isValidPassword(password: String): Boolean {
        // Valida se a senha tem pelo menos 8 caracteres
        if (password.length < 8) return false

        // Valida se a senha contém pelo menos um dígito
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return false

        // Valida se a senha contém pelo menos um caractere especial
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        if (!hasSpecialChar) return false

        // Valida se a senha contém pelo menos uma letra maiúscula
        val hasUppercase = password.any { it.isUpperCase() }
        if (!hasUppercase) return false

        // Valida se a senha contém pelo menos uma letra minúscula
        val hasLowercase = password.any { it.isLowerCase() }
        if (!hasLowercase) return false

        return true
    }
}
