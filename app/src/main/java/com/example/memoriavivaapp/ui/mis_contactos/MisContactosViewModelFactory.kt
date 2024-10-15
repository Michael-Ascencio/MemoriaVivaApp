package com.example.memoriavivaapp.ui.mis_contactos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MisContactosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisContactosViewModel::class.java)) {
            return MisContactosViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
