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

    // Obtener todas las notas desde la base de datos
    fun obtenerNotas() {
        CoroutineScope(Dispatchers.IO).launch {
            val notasFromDb = dbHelper.obtenerNotas()
            _notas.postValue(notasFromDb)
        }
    }

    // Agregar una nueva nota
    fun agregarNota(titulo: String, contenido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.insertarNota(titulo, contenido)
            obtenerNotas() // Actualiza la lista después de agregar
        }
    }

    // Editar una nota existente
    fun editarNota(id: Int, titulo: String, contenido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.editarNota(id, titulo, contenido)
            obtenerNotas() // Actualiza la lista después de editar
        }
    }

    // Eliminar una nota
    fun eliminarNota(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.eliminarNota(id)
            obtenerNotas() // Actualiza la lista después de eliminar
        }
    }
}
