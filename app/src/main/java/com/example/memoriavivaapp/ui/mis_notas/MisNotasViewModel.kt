package com.example.memoriavivaapp.ui.mis_notas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MisNotasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de mis notas"
    }
    val text: LiveData<String> = _text
}