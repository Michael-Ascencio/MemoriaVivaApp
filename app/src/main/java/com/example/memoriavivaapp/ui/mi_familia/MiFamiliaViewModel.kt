package com.example.memoriavivaapp.ui.mi_familia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiFamiliaViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = FamiliaSQLiteHelper(application)
    private val _familiares = MutableLiveData<List<Familiar>>()
    val familiares: LiveData<List<Familiar>> = _familiares

    // Obtener la lista de familiares desde la base de datos
    fun obtenerFamiliares() {
        CoroutineScope(Dispatchers.IO).launch {
            val listaFamiliares = dbHelper.obtenerFamiliares()
            _familiares.postValue(listaFamiliares)
        }
    }

    // Agregar un nuevo familiar a la base de datos
    fun agregarFamiliar(familiar: Familiar) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.insertarFamiliar(familiar)
            obtenerFamiliares() // Actualiza la lista después de agregar
        }
    }

    // Actualizar los datos de un familiar en la base de datos
    fun actualizarFamiliar(familiar: Familiar) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.actualizarFamiliar(familiar)
            obtenerFamiliares() // Actualiza la lista después de actualizar
        }
    }

    // Eliminar un familiar de la base de datos
    fun eliminarFamiliar(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.eliminarFamiliar(id)
            obtenerFamiliares() // Actualiza la lista después de eliminar
        }
    }
}
