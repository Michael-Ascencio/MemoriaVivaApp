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
        adapter = NotaAdapter(emptyList()) { nota: Nota ->

        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}