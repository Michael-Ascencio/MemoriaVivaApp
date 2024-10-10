package com.example.memoriavivaapp.ui.medicamentos_usados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentMedUsadosBinding
import com.example.memoriavivaapp.ui.conf_notifiaciones.ConfNotificacionesViewModel

class MedUsadosFragment : Fragment() {

    private var _binding: FragmentMedUsadosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misNotasViewModel =
            ViewModelProvider(this).get(MedUsadosViewModel::class.java)

        _binding = FragmentMedUsadosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMedUsados
        misNotasViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}