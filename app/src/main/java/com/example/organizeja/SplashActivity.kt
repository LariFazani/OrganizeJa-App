package com.example.organizeja

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    // Tempo de exibição da tela de splash em milissegundos (2 segundos)
    private val SPLASH_TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Oculta a barra de título (ActionBar/Toolbar) para aparecer em tela cheia
        supportActionBar?.hide()

        // Usa um Handler para atrasar a transição para a CadastroActivity
        Handler(Looper.getMainLooper()).postDelayed({

            // Cria uma Intent para iniciar a CadastroActivity
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)

            // Finaliza a SplashActivity para que o usuário não possa voltar para ela
            finish()
        }, SPLASH_TIME_OUT)
    }
}