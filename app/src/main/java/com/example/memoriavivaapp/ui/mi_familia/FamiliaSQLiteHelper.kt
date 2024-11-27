package com.example.memoriavivaapp.ui.mi_familia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FamiliaSQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FamiliaDB"
        private const val TABLE_FAMILIA = "familia"
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_PARENTECO = "parentesco"
        private const val KEY_DESCRIPCION = "descripcion"
        private const val KEY_FOTO = "foto"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FAMILIA_TABLE = ("CREATE TABLE $TABLE_FAMILIA ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_NOMBRE TEXT,"
                + "$KEY_PARENTECO TEXT,"
                + "$KEY_DESCRIPCION TEXT,"
                + "$KEY_FOTO TEXT)")
        db?.execSQL(CREATE_FAMILIA_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAMILIA")
        onCreate(db)
    }

    fun insertarFamiliar(familiar: Familiar): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE, familiar.nombre)
            put(KEY_PARENTECO, familiar.parentesco)
            put(KEY_DESCRIPCION, familiar.descripcion)
            put(KEY_FOTO, familiar.foto)
        }
        return db.insert(TABLE_FAMILIA, null, values)
    }

    fun obtenerFamiliares(): List<Familiar> {
        val listaFamiliares: MutableList<Familiar> = ArrayList()
        val query = "SELECT * FROM $TABLE_FAMILIA"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val familiar = Familiar(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE)),
                    parentesco = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PARENTECO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPCION)),
                    foto = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FOTO))
                )
                listaFamiliares.add(familiar)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaFamiliares
    }

    fun actualizarFamiliar(familiar: Familiar): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE, familiar.nombre)
            put(KEY_PARENTECO, familiar.parentesco)
            put(KEY_DESCRIPCION, familiar.descripcion)
            put(KEY_FOTO, familiar.foto)
        }
        return db.update(TABLE_FAMILIA, values, "$KEY_ID = ?", arrayOf(familiar.id.toString()))
    }

    fun eliminarFamiliar(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_FAMILIA, "$KEY_ID = ?", arrayOf(id.toString()))
    }
}
