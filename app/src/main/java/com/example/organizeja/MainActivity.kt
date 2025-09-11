package com.example.organizeja

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.organizeja.databinding.ActivityMainBinding

// Declaração da Classe
class MainActivity : AppCompatActivity() {

    // Variável de Binding
    private lateinit var binding: ActivityMainBinding

    // Método onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar o Layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referência à BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        // Configuração do NavController
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Vincular a BottomNavigationView ao NavController
        navView.setupWithNavController(navController)

        // Adiciona um listener para o destino da navegação
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Se o destino for a tela de adicionar transação, oculte a ActionBar
                R.id.navigation_adicionar_transacao -> {
                    supportActionBar?.hide()
                    binding.navView.visibility = View.GONE
                }
                // Para todas as outras telas (início, transações, configurações), mostre a ActionBar
                else -> {
                    supportActionBar?.show()
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }
    }
}