package com.example.memoriavivaapp.ui.mis_contactos

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MisContactosViewModel(context1: Context) : ViewModel() {

    private lateinit var contactoDatabaseHelper: ContactoDatabaseHelper

    // Propiedad para el mensaje de instrucción
    val mensajeInstruccion: String = "Para llamar a un contacto, da click sobre el contacto deseado."

    // Inicializa el ContactoDatabaseHelper
    fun initDatabase(context: Context) {
        contactoDatabaseHelper = ContactoDatabaseHelper(context)
    }

    // Método para agregar un contacto
    fun agregarContacto(nombre: String, telefono: String) {
        val db = contactoDatabaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(ContactoDatabaseHelper.COLUMN_NOMBRE, nombre)
            put(ContactoDatabaseHelper.COLUMN_TELEFONO, telefono)
        }
        db.insert(ContactoDatabaseHelper.TABLE_NAME, null, values)
        db.close() // Cerrar la base de datos después de la inserción
    }
}
