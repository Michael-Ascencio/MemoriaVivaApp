package com.example.memoriavivaapp.ui.mis_contactos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentEditarContactoBinding

class EditarContactoFragment : Fragment() {

    private var _binding: FragmentEditarContactoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MisContactosViewModel

    private var contactoId: Long = -1
    private var contactoNombre: String? = null
    private var contactoTelefono: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarContactoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener argumentos desde el Bundle
        arguments?.let {
            contactoId = it.getLong("id", -1)
            contactoNombre = it.getString("nombre")
            contactoTelefono = it.getString("telefono")
        }

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this, MisContactosViewModelFactory(requireContext()))
            .get(MisContactosViewModel::class.java)

        // Mostrar datos del contacto en los campos de entrada
        binding.inputNombre.setText(contactoNombre)
        binding.inputTelefono.setText(contactoTelefono)

        // Configurar el botón de guardar
        binding.btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun guardarCambios() {
        val nuevoNombre = binding.inputNombre.text.toString().trim()
        val nuevoTelefono = binding.inputTelefono.text.toString().trim()

        if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al ViewModel para actualizar el contacto
        viewModel.initDatabase(requireContext())
        viewModel.actualizarContacto(contactoId, nuevoNombre, nuevoTelefono)

        Toast.makeText(requireContext(), "Contacto actualizado", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Volver al fragmento anterior
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
