package com.example.memoriavivaapp.ui.mis_contactos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MisContactosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisContactosViewModel::class.java)) {
            val helper = ContactoDatabaseHelper(context)
            @Suppress("UNCHECKED_CAST")
            return MisContactosViewModel(helper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

