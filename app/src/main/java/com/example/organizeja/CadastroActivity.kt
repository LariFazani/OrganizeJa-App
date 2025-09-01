package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
            Log.d("CadastroActivity", "Botão Prosseguir clicado.")

            val email = binding.emailEditText.text.toString().trim()
            val cpf = binding.cpfEditText.text.toString().replace("[^\\d]".toRegex(), "")
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validação dos campos
            if (email.isEmpty() || cpf.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Log.d("CadastroActivity", "Campos obrigatórios vazios.")
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.d("CadastroActivity", "Formato de e-mail inválido.")
                Toast.makeText(this, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validarCpf(cpf)) {
                Log.d("CadastroActivity", "CPF inválido.")
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Log.d("CadastroActivity", "As senhas não coincidem.")
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Log.d("CadastroActivity", "Senha não atende aos critérios de validação.")
                Toast.makeText(this, "A senha deve ter no mínimo 8 caracteres, com pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Log.d("CadastroActivity", "Todas as validações de campos passadas. Tentando criar usuário no Firebase.")
            // Tenta criar o usuário com e-mail e senha no Firebase Auth
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Usuário criado com sucesso
                        Log.d("CadastroActivity", "Usuário criado com sucesso no Firebase Auth.")
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            // Tenta salvar os dados adicionais do usuário no Firestore
                            salvarDadosUsuario(user, email, cpf)
                        } else {
                            Log.e("CadastroActivity", "Erro: Usuário nulo após criação bem-sucedida.")
                            navigateToLogin()
                        }
                    } else {
                        // Se o cadastro falhar, exibe uma mensagem para o usuário
                        Log.e("CadastroActivity", "Erro no cadastro do usuário: ${task.exception?.message}")
                        Toast.makeText(this, "Erro no cadastro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        navigateToLogin()
                    }
                }
        }

        // Lógica para o link "Já tem uma conta? Faça login"
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Formatação de CPF
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

    private fun salvarDadosUsuario(user: FirebaseUser, email: String, cpf: String) {
        Log.d("CadastroActivity", "Tentando salvar dados do usuário no Firestore.")
        val userData = hashMapOf(
            "email" to email,
            "cpf" to cpf
        )
        // O ID do documento é o UID do usuário do Firebase Auth
        firebaseFirestore.collection("usuarios").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d("CadastroActivity", "Dados do usuário salvos com sucesso no Firestore.")
                Toast.makeText(this, "Cadastro e dados salvos com sucesso!", Toast.LENGTH_LONG).show()
                navigateToLogin()
            }
            .addOnFailureListener { e ->
                Log.e("CadastroActivity", "Erro ao salvar dados do usuário no Firestore: ${e.message}")
                Toast.makeText(this, "Erro ao salvar dados do usuário: ${e.message}", Toast.LENGTH_LONG).show()
                // A navegação agora é independente da falha no Firestore
                navigateToLogin()
            }
    }

    private fun navigateToLogin() {
        Log.d("CadastroActivity", "Iniciando intent para LoginActivity.")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Encerra a atividade atual para que o usuário não possa voltar
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
        if (password.length < 8) {
            Log.d("isValidPassword", "Senha muito curta.")
            return false
        }

        // Valida se a senha contém pelo menos um dígito
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            Log.d("isValidPassword", "Senha não contém dígito.")
            return false
        }

        // Valida se a senha contém pelo menos um caractere especial
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        if (!hasSpecialChar) {
            Log.d("isValidPassword", "Senha não contém caractere especial.")
            return false
        }

        // Valida se a senha contém pelo menos uma letra maiúscula
        val hasUpperCase = password.any { it.isUpperCase() }
        if (!hasUpperCase) {
            Log.d("isValidPassword", "Senha não contém letra maiúscula.")
            return false
        }

        // Valida se a senha contém pelo menos uma letra minúscula
        val hasLowerCase = password.any { it.isLowerCase() }
        if (!hasLowerCase) {
            Log.d("isValidPassword", "Senha não contém letra minúscula.")
            return false
        }
        Log.d("isValidPassword", "Senha válida.")
        return true
    }
}
