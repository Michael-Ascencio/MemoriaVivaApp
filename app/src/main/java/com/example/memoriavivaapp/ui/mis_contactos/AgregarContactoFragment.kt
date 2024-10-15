package com.example.memoriavivaapp.ui.mis_contactos

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.memoriavivaapp.databinding.FragmentAgregarContactoBinding
import java.util.*

class AgregarContactoFragment : Fragment() {

    private var _binding: FragmentAgregarContactoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisContactosViewModel

    private lateinit var speechActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var isListeningToName = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarContactoBinding.inflate(inflater, container, false)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this, MisContactosViewModelFactory(requireContext())).get(MisContactosViewModel::class.java)

        // Inicializa la base de datos
        viewModel.initDatabase(requireContext())

        // Registrar el lanzador para el reconocimiento de voz
        speechActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data: Intent? = result.data
                val resultList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!resultList.isNullOrEmpty()) {
                    if (isListeningToName) {
                        binding.editTextNombre.setText(resultList[0])
                    } else {
                        binding.editTextTelefono.setText(resultList[0])
                    }
                }
            }
        }

        // Registrar el lanzador para los permisos
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                iniciarReconocimientoVoz()
            } else {
                Toast.makeText(requireContext(), "Permiso de audio denegado", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Guardar contacto al hacer clic en el botón
        binding.buttonGuardarContacto.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString()
            val telefono = binding.editTextTelefono.text.toString()
            val telefonoLimpio = telefono.replace(Regex("[^0-9]"), "")

            if (nombre.isNotBlank() && telefonoLimpio.isNotBlank()) {
                viewModel.agregarContacto(nombre, telefonoLimpio)
                binding.buttonLlamarContacto.visibility = View.VISIBLE // Mostrar el botón de llamar
                binding.buttonLlamarContacto.setOnClickListener {
                    realizarLlamada(telefonoLimpio) // Llamar al número
                }
                findNavController().navigateUp() // Volver al fragmento de contactos
            } else {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar los botones de escucha
        binding.btnEscucharNombre.setOnClickListener {
            isListeningToName = true
            checkAndRequestPermissions()
        }

        binding.btnEscucharTelefono.setOnClickListener {
            isListeningToName = false
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            iniciarReconocimientoVoz()
        }
    }

    private fun iniciarReconocimientoVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("es", "ES"))
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")

        try {
            speechActivityResultLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Tu dispositivo no soporta reconocimiento de voz", Toast.LENGTH_SHORT).show()
        }
    }

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
