package com.example.memoriavivaapp.ui.mis_contactos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MisContactosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de mis contactos"
    }
    val text: LiveData<String> = _text
}