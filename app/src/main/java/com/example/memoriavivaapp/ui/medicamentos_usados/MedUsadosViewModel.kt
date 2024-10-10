package com.example.memoriavivaapp.ui.medicamentos_usados

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedUsadosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de medicamentos usados"
    }
    val text: LiveData<String> = _text
}