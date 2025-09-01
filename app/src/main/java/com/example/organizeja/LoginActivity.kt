package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.organizeja.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout usando View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Oculta a barra de título (ActionBar/Toolbar)
        supportActionBar?.hide()

        // Lógica para o botão "Prosseguir"
        binding.prosseguirButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            // Validação dos campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Realiza o login com e-mail e senha usando o Firebase Auth
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()

                    // Navega para a MainActivity após o login bem-sucedido
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                } else {
                    // Exibe uma mensagem de erro em caso de falha na autenticação
                    Toast.makeText(this, "Falha na autenticação: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Lógica para o link "Esqueci minha senha"
        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Lógica para o link "Cadastro"
        binding.cadastroTextView.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}