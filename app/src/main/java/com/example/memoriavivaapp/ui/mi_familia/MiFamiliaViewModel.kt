package com.example.memoriavivaapp.ui.mi_familia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MiFamiliaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "este es el fragmento de mi familia"
    }
    val text: LiveData<String> = _text
}