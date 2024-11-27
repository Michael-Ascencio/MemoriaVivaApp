package com.example.memoriavivaapp.ui.mi_familia

import FamiliarAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoriavivaapp.databinding.FragmentMiFamiliaBinding
import com.example.memoriavivaapp.R

class MiFamiliaFragment : Fragment() {

    private var _binding: FragmentMiFamiliaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MiFamiliaViewModel
    private lateinit var adapter: FamiliarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiFamiliaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this).get(MiFamiliaViewModel::class.java)

        // Inicializar el adaptador con una lista vacía
        adapter = FamiliarAdapter(
            familiares = emptyList(), // Lista vacía al principio
            onEditClick = { familiar ->
                // Crear un Bundle con los parámetros
                val bundle = Bundle().apply {
                    putLong("id", familiar.id)
                    putString("nombre", familiar.nombre)
                    putString("parentesco", familiar.parentesco)
                    putString("descripcion", familiar.descripcion)
                }

                // Navegar al fragmento de editar familiar y pasar el Bundle
                findNavController().navigate(R.id.actionMiFamiliaFragmentToEditarFamiliarFragment, bundle)
            },
            onDeleteClick = { familiar ->
                // Eliminar familiar
                viewModel.eliminarFamiliar(familiar.id)
            }
        )

        // Configurar RecyclerView
        binding.recyclerViewFamiliares.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFamiliares.adapter = adapter

        // Observar cambios en la lista de familiares
        viewModel.familiares.observe(viewLifecycleOwner) { familiares ->
            // Actualizar la lista usando submitList
            adapter.submitList(familiares)
        }

        // Obtener la lista de familiares desde el ViewModel
        viewModel.obtenerFamiliares()

        // Navegar a agregar un nuevo familiar
        binding.fabAgregarFamiliar.setOnClickListener {
            findNavController().navigate(R.id.actionMiFamiliaFragmentToAgregarFamiliarFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
