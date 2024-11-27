package com.example.memoriavivaapp.ui.mi_familia

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.memoriavivaapp.databinding.FragmentAgregarFamiliarBinding

class AgregarFamiliarFragment : Fragment() {

    private var _binding: FragmentAgregarFamiliarBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MiFamiliaViewModel
    private var fotoUri: Uri? = null

    // Códigos de solicitud
    private val REQUEST_CAMERA_PERMISSION = 1
    private val REQUEST_GALLERY_PERMISSION = 2
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarFamiliarBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MiFamiliaViewModel::class.java)

        // Botón para tomar foto con la cámara
        binding.btnTomarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }

        // Botón para seleccionar foto desde la galería
        binding.btnSeleccionarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_GALLERY_PERMISSION
                )
            }
        }

        // Guardar los cambios
        binding.btnGuardarFamiliar.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString()
            val parentesco = binding.editTextParentesco.text.toString()
            val descripcion = binding.editTextDescripcion.text.toString()

            if (nombre.isNotBlank() && parentesco.isNotBlank() && descripcion.isNotBlank()) {
                val familiar = Familiar(
                    nombre = nombre,
                    parentesco = parentesco,
                    descripcion = descripcion,
                    foto = fotoUri.toString() // Guardar la URI de la foto
                )
                viewModel.agregarFamiliar(familiar)
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Función para abrir la cámara
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nueva Foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto tomada desde la cámara")
        fotoUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    // Función para abrir la galería
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    // Manejo de los resultados de las actividades de cámara o galería
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // Mostrar la imagen tomada en el ImageView
                    binding.imageViewFoto.setImageURI(fotoUri)
                }
                GALLERY_REQUEST_CODE -> {
                    // Obtener la URI de la imagen seleccionada desde la galería
                    fotoUri = data?.data
                    binding.imageViewFoto.setImageURI(fotoUri) // Mostrar la foto seleccionada
                }
            }
        }
    }

    // Manejo de permisos en tiempo de ejecución
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(requireContext(), "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
