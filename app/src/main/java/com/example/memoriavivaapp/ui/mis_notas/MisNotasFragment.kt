package com.example.memoriavivaapp.ui.mis_notas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoriavivaapp.databinding.FragmentMisNotasBinding
import androidx.navigation.fragment.findNavController
import com.example.memoriavivaapp.R
import android.app.AlertDialog

class MisNotasFragment : Fragment() {

    private var _binding: FragmentMisNotasBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MisNotasViewModel
    private lateinit var adapter: NotaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisNotasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewNotas.layoutManager = LinearLayoutManager(context)

        // Inicializa el adaptador con una lista vacía y un click listener
        adapter = NotaAdapter(emptyList(),
            onEditClick = { nota ->
                // Navegar al fragmento de edición y pasar los datos de la nota
                val bundle = Bundle().apply {
                    putInt("id", nota.id)
                    putString("titulo", nota.titulo)
                    putString("contenido", nota.contenido)
                }
                findNavController().navigate(R.id.editarNotaFragment, bundle)
            },
            onDeleteClick = { nota ->
                // Muestra un diálogo de confirmación antes de eliminar la nota
                showDeleteConfirmationDialog(nota)
            }
        )

        binding.recyclerViewNotas.adapter = adapter

        // Observa las notas desde el ViewModel
        viewModel = ViewModelProvider(this).get(MisNotasViewModel::class.java)
        viewModel.notas.observe(viewLifecycleOwner) {
            adapter.setNotas(it) // Actualiza el adaptador con las nuevas notas
        }
        viewModel.obtenerNotas()

        // Navegar a crear una nueva nota al hacer clic en el botón
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.agregarNotaFragment)
        }
    }

    private fun showDeleteConfirmationDialog(nota: Nota) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("¿Estás seguro de que deseas eliminar esta nota?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.eliminarNota(nota.id) // Elimina la nota si el usuario confirma
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo si el usuario cancela
            }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
