package com.example.memoriavivaapp.ui.mis_notas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentMisNotasBinding

class MisNotasFragment : Fragment() {

    private var _binding: FragmentMisNotasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misNotasViewModel =
            ViewModelProvider(this).get(MisNotasViewModel::class.java)

        _binding = FragmentMisNotasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotas
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