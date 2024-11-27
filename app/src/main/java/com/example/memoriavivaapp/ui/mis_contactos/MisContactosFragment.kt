package com.example.memoriavivaapp.ui.mis_contactos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoriavivaapp.R
import com.example.memoriavivaapp.databinding.FragmentMisContactosBinding
import androidx.navigation.fragment.findNavController

class MisContactosFragment : Fragment() {

    private var _binding: FragmentMisContactosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisContactosViewModel
    private lateinit var contactoDatabaseHelper: ContactoDatabaseHelper
    private lateinit var adapter: ContactoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisContactosBinding.inflate(inflater, container, false)

        // Inicializar el ViewModel usando el Factory
        viewModel = ViewModelProvider(
            this,
            MisContactosViewModelFactory(requireContext())
        ).get(MisContactosViewModel::class.java)

        // Inicializar el helper de base de datos
        contactoDatabaseHelper = ContactoDatabaseHelper(requireContext())

        return binding.root
    }

    private fun recargarContactos() {
        val contactos = obtenerContactos()
        adapter = ContactoAdapter(contactos,
            onEditClick = { contacto ->
                val bundle = Bundle().apply {
                    putLong("id", contacto.id)
                    putString("nombre", contacto.nombre)
                    putString("telefono", contacto.telefono)
                }
                findNavController().navigate(R.id.editarContactoFragment, bundle)
            },
            onDeleteClick = { contacto ->
                eliminarContacto(contacto.id)
                recargarContactos()
            }
        )
        binding.recyclerViewContactos.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewContactos.layoutManager = LinearLayoutManager(context)

        // Obtener la lista de contactos desde la base de datos
        val contactos = obtenerContactos()

        // Mostrar mensaje si no hay contactos
        if (contactos.isEmpty()) {
            binding.textNoContactos.visibility = View.VISIBLE
            binding.recyclerViewContactos.visibility = View.GONE
        } else {
            binding.textNoContactos.visibility = View.GONE
            binding.recyclerViewContactos.visibility = View.VISIBLE

            adapter = ContactoAdapter(contactos,
                onEditClick = { contacto ->
                    val bundle = Bundle().apply {
                        putLong("id", contacto.id)
                        putString("nombre", contacto.nombre)
                        putString("telefono", contacto.telefono)
                    }
                    findNavController().navigate(R.id.editarContactoFragment, bundle)
                },
                onDeleteClick = { contacto ->
                    eliminarContacto(contacto.id)
                    recargarContactos()
                }
            )
            binding.recyclerViewContactos.adapter = adapter
        }

        // Configurar el botón flotante para agregar contactos
        binding.fabAgregarContacto.setOnClickListener {
            findNavController().navigate(R.id.agregarContactoFragment)
        }
    }

    // Método para obtener los contactos desde la base de datos SQLite
    private fun obtenerContactos(): List<Contacto> {
        val db = contactoDatabaseHelper.readableDatabase
        val cursor = db.query(
            ContactoDatabaseHelper.TABLE_NAME, null, null, null, null, null, null
        )
        val contactos = mutableListOf<Contacto>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactoDatabaseHelper.COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(ContactoDatabaseHelper.COLUMN_NOMBRE))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow(ContactoDatabaseHelper.COLUMN_TELEFONO))
            contactos.add(Contacto(id, nombre, telefono))
        }
        cursor.close()
        return contactos
    }

    // Método para eliminar un contacto de la base de datos SQLite
    private fun eliminarContacto(id: Long) {
        val db = contactoDatabaseHelper.writableDatabase
        db.delete(
            ContactoDatabaseHelper.TABLE_NAME,
            "${ContactoDatabaseHelper.COLUMN_ID}=?",
            arrayOf(id.toString())
        )
    }

    // Método para realizar una llamada telefónica
    private fun realizarLlamada(numero: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$numero")
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
