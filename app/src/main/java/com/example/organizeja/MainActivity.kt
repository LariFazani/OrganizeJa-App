package com.example.organizeja

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
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
    }
}
