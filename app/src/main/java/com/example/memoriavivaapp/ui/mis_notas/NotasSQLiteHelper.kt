import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.memoriavivaapp.ui.mis_notas.Nota

class NotasSQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NotasDB"
        private const val TABLE_NOTAS = "notas"
        private const val KEY_ID = "id"
        private const val KEY_TITULO = "titulo"
        private const val KEY_CONTENIDO = "contenido"
        private const val KEY_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_NOTAS_TABLE = ("CREATE TABLE $TABLE_NOTAS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_TITULO TEXT,"
                + "$KEY_CONTENIDO TEXT,"
                + "$KEY_FECHA INTEGER)")
        db?.execSQL(CREATE_NOTAS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTAS")
        onCreate(db)
    }

    // Insertar una nueva nota
    fun insertarNota(titulo: String, contenido: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITULO, titulo)
        values.put(KEY_CONTENIDO, contenido)
        values.put(KEY_FECHA, System.currentTimeMillis())
        return db.insert(TABLE_NOTAS, null, values)
    }

    // Obtener todas las notas
    fun obtenerNotas(): List<Nota> {
        val listaNotas: MutableList<Nota> = ArrayList()
        val query = "SELECT * FROM $TABLE_NOTAS ORDER BY $KEY_FECHA DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val nota = Nota(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITULO)),
                    contenido = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENIDO)),
                    fecha = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_FECHA))
                )
                listaNotas.add(nota)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaNotas
    }

    // Editar una nota existente
    fun editarNota(id: Int, titulo: String, contenido: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITULO, titulo)
        values.put(KEY_CONTENIDO, contenido)
        // No actualizamos la fecha para mantener la original
        return db.update(TABLE_NOTAS, values, "$KEY_ID = ?", arrayOf(id.toString()))
    }

    // Eliminar una nota
    fun eliminarNota(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NOTAS, "$KEY_ID = ?", arrayOf(id.toString()))
    }
}
