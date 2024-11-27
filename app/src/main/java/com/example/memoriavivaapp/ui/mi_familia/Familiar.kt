package com.example.memoriavivaapp.ui.mi_familia

import java.io.Serializable

data class Familiar(
    val id: Long = 0,
    val nombre: String,
    val parentesco: String,
    val descripcion: String,
    val foto: String // Usaremos una ruta de archivo o URI para la foto
) : Serializable

