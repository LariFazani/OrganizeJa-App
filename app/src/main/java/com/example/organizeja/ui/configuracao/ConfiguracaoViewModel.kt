package com.example.organizeja.ui.configuracao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfiguracaoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Configuração Fragment"
    }
    val text: LiveData<String> = _text
}