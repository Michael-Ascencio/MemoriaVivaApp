package com.example.memoriavivaapp.ui.mi_familia

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentEditarFamiliarBinding
import com.example.memoriavivaapp.R

class EditarFamiliarFragment : Fragment() {

    private var _binding: FragmentEditarFamiliarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MiFamiliaViewModel

    private var familiarId: Long = -1
    private var familiarNombre: String? = null
    private var familiarParentesco: String? = null
    private var familiarDescripcion: String? = null
    private var fotoUri: Uri? = null

    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                fotoUri = it
                binding.imageViewFoto.setImageURI(it) // Mostrar la foto seleccionada
            }
        }

    private val tomarFoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    val uri = guardarImagenEnGaleria(it)
                    fotoUri = uri
                    binding.imageViewFoto.setImageBitmap(it) // Mostrar la foto tomada
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarFamiliarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener los argumentos desde el Bundle
        arguments?.let {
            familiarId = it.getLong("id", -1)
            familiarNombre = it.getString("nombre")
            familiarParentesco = it.getString("parentesco")
            familiarDescripcion = it.getString("descripcion")
            val foto = it.getString("foto") // Obtener la URI de la foto si existe
            fotoUri = if (!foto.isNullOrEmpty()) Uri.parse(foto) else null // Parsear la URI de la foto

            // Mostrar la foto en el ImageView, si existe una URI
            fotoUri?.let { uri ->
                binding.imageViewFoto.setImageURI(uri)
            } ?: run {
                // Si no hay URI, mostrar la imagen predeterminada
                binding.imageViewFoto.setImageResource(R.drawable.person_book)
            }
        }

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this).get(MiFamiliaViewModel::class.java)

        // Mostrar los datos del familiar en los campos de entrada
        binding.editTextNombre.setText(familiarNombre)
        binding.editTextParentesco.setText(familiarParentesco)
        binding.editTextDescripcion.setText(familiarDescripcion)

        // Configurar los botones para tomar o seleccionar foto
        binding.btnSeleccionarFoto.setOnClickListener {
            seleccionarImagenDesdeGaleria()
        }

        binding.btnTomarFoto.setOnClickListener {
            tomarFotoConCamara()
        }

        // Configurar el botón de guardar
        binding.btnGuardarFamiliar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun seleccionarImagenDesdeGaleria() {
        seleccionarImagen.launch("image/*")
    }

    private fun tomarFotoConCamara() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            tomarFoto.launch(intent)
        }
    }

    private fun guardarCambios() {
        val nuevoNombre = binding.editTextNombre.text.toString().trim()
        val nuevoParentesco = binding.editTextParentesco.text.toString().trim()
        val nuevaDescripcion = binding.editTextDescripcion.text.toString().trim()

        if (nuevoNombre.isEmpty() || nuevoParentesco.isEmpty() || nuevaDescripcion.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al ViewModel para actualizar el familiar
        viewModel.actualizarFamiliar(
            Familiar(
                id = familiarId,
                nombre = nuevoNombre,
                parentesco = nuevoParentesco,
                descripcion = nuevaDescripcion,
                foto = fotoUri?.toString() ?: "" // Pasar la URI de la foto como String
            )
        )

        Toast.makeText(requireContext(), "Familiar actualizado", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Volver al fragmento anterior
    }

    private fun guardarImagenEnGaleria(bitmap: Bitmap): Uri? {
        val resolver = requireContext().contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "familiar_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MemoriaViva")
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } ?: run {
                Toast.makeText(requireContext(), "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
        return uri
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
}
