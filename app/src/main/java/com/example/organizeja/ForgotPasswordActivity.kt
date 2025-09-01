package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var proceedButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Inicializa as referências dos componentes do layout
        emailEditText = findViewById(R.id.emailEditText)
        proceedButton = findViewById(R.id.prosseguirButton)
        backButton = findViewById(R.id.backButton)

        // Define o clique do botão "Prosseguir"
        proceedButton.setOnClickListener {
            // Lógica para enviar o código de recuperação
            val email = emailEditText.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Instruções de recuperação enviadas!", Toast.LENGTH_SHORT).show()
            }
        }

        // Define o clique do botão "Voltar"
        backButton.setOnClickListener {
            // Volta para a tela de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
