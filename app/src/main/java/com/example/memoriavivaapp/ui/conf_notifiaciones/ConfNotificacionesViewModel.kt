package com.example.memoriavivaapp.ui.conf_notifiaciones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfNotificacionesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento de la configuracion de notificaciones"
    }
    val text: LiveData<String> = _text
}