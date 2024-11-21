package com.example.memoriavivaapp.ui.mis_notas

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.memoriavivaapp.databinding.FragmentEditarNotaBinding
import java.util.*

class EditarNotaFragment : Fragment() {

    private var _binding: FragmentEditarNotaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisNotasViewModel

    private lateinit var speechActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var isListeningToTitle = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarNotaBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MisNotasViewModel::class.java)

        // Registrar el lanzador para el reconocimiento de voz
        speechActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data: Intent? = result.data
                val resultList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!resultList.isNullOrEmpty()) {
                    if (isListeningToTitle) {
                        binding.editTextTitulo.setText(resultList[0])
                    } else {
                        binding.editTextContenido.setText(resultList[0])
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

        // Recuperar datos enviados desde el fragmento anterior
        val id = arguments?.getInt("id") ?: 0
        val titulo = arguments?.getString("titulo") ?: ""
        val contenido = arguments?.getString("contenido") ?: ""

        // Poner los datos actuales en los campos
        binding.editTextTitulo.setText(titulo)
        binding.editTextContenido.setText(contenido)

        // Guardar los cambios al hacer clic en el botón
        binding.buttonEditarNota.setOnClickListener {
            val nuevoTitulo = binding.editTextTitulo.text.toString()
            val nuevoContenido = binding.editTextContenido.text.toString()

            if (nuevoTitulo.isNotBlank() && nuevoContenido.isNotBlank()) {
                viewModel.editarNota(id, nuevoTitulo, nuevoContenido)
                findNavController().navigateUp() // Volver al fragmento de las notas
            } else {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEscucharContenido.setOnClickListener {
            isListeningToTitle = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
