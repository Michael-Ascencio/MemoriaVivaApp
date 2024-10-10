package com.example.memoriavivaapp.ui.conf_notificaciones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentConfNotificacionesBinding
import com.example.memoriavivaapp.ui.conf_notifiaciones.ConfNotificacionesViewModel

class ConfNotificacionesFragment : Fragment() {

    private var _binding: FragmentConfNotificacionesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misNotasViewModel =
            ViewModelProvider(this).get(ConfNotificacionesViewModel::class.java)

        _binding = FragmentConfNotificacionesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotificaciones
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