package com.example.memoriavivaapp.ui.conf_notifiaciones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

data class Recordatorio(
    val descripcion: String,
    val fecha: Long,
    val hora: Long
)

class ConfNotificacionesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de la configuración de notificaciones"
    }
    val text: LiveData<String> = _text

    // Cambiar MutableLiveData de MutableList a List
    private val _recordatorios = MutableLiveData<List<Recordatorio>>().apply {
        value = emptyList() // Inicializar con una lista vacía
    }

    // Propiedad pública que expone la lista de recordatorios como LiveData
    val recordatorios: LiveData<List<Recordatorio>> = _recordatorios

    // Método para agregar un nuevo recordatorio
    fun agregarRecordatorio(descripcion: String, fecha: Long, hora: Long) {
        // Crear un nuevo recordatorio
        val nuevoRecordatorio = Recordatorio(descripcion, fecha, hora)

        // Obtener la lista actual de recordatorios y agregar el nuevo
        val currentList = _recordatorios.value?.toMutableList() ?: mutableListOf()
        currentList.add(nuevoRecordatorio)

        // Actualizar el LiveData con la nueva lista
        _recordatorios.value = currentList
    }

    // Método para obtener los recordatorios
    fun obtenerRecordatorios(): LiveData<List<Recordatorio>> {
        return recordatorios
    }
}
