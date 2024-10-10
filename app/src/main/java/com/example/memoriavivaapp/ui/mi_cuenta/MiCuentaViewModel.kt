package com.example.memoriavivaapp.ui.mi_cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MiCuentaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de mi cuenta"
    }
    val text: LiveData<String> = _text
}