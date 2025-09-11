package com.example.organizeja

import android.content.Intent
import android.os.Bundle
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

        // Lógica para o link "login"
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Lógica para o botão "Prosseguir"
        binding.prosseguirButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val nomeusuario = binding.nomeusuarioEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validações com Toast e retorno precoce
            if (email.isEmpty() || nomeusuario.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isValidPassword(password)) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres, um dígito, um caractere especial, uma letra maiúscula e uma minúscula.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Se todas as validações passarem, continua com o cadastro
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.let {
                            salvarDadosUsuario(it, nomeusuario, email)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Falha no cadastro: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    // Função de validação de senha
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return false
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        if (!hasSpecialChar) return false
        val hasUppercase = password.any { it.isUpperCase() }
        if (!hasUppercase) return false
        val hasLowercase = password.any { it.isLowerCase() }
        if (!hasLowercase) return false
        return true
    }

    // Função para salvar dados do usuário no Firestore
    private fun salvarDadosUsuario(user: FirebaseUser, nomeUsuario: String, email: String) {
        val userData = hashMapOf(
            "nomeUsuario" to nomeUsuario,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        firebaseFirestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ao salvar dados do usuário: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}
