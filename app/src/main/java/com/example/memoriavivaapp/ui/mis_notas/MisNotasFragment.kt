package com.example.memoriavivaapp.ui.mis_notas

import NotaAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoriavivaapp.databinding.FragmentMisNotasBinding

data class Nota(
    val titulo: String,
    val contenido: String
)

class MisNotasFragment : Fragment() {

    private var _binding: FragmentMisNotasBinding? = null
    private val binding get() = _binding!!

    private lateinit var notaAdapter: NotaAdapter
    private val listaNotas = mutableListOf<Nota>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val misNotasViewModel =
            ViewModelProvider(this).get(MisNotasViewModel::class.java)

        _binding = FragmentMisNotasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar RecyclerView
        val recyclerView = binding.recyclerViewNotas
        recyclerView.layoutManager = LinearLayoutManager(context)

        notaAdapter = NotaAdapter(listaNotas) { nota ->
            // Aquí manejas el clic en una nota
            mostrarNota(nota)
        }
        recyclerView.adapter = notaAdapter

        // Agregar notas de ejemplo
        listaNotas.add(Nota("Nota 1", "Contenido de la nota 1"))
        listaNotas.add(Nota("Nota 2", "Contenido de la nota 2"))

        notaAdapter.notifyDataSetChanged()

        return root
    }

    private fun mostrarNota(nota: Nota) {
        // Aquí navegas o muestras el contenido de la nota
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
