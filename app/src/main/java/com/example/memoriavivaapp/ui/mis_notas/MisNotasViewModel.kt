package com.example.memoriavivaapp.ui.mis_notas

import NotasSQLiteHelper
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MisNotasViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = NotasSQLiteHelper(application)

    private val _notas = MutableLiveData<List<Nota>>()
    val notas: LiveData<List<Nota>> = _notas

    fun obtenerNotas() {
        CoroutineScope(Dispatchers.IO).launch {
            val notasFromDb = dbHelper.obtenerNotas()
            _notas.postValue(notasFromDb)
        }
    }

    fun agregarNota(titulo: String, contenido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.insertarNota(titulo, contenido)
            obtenerNotas() // Actualiza la lista
        }
    }
}
