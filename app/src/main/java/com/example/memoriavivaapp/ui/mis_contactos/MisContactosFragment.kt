package com.example.memoriavivaapp.ui.mis_contactos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentMisContactosBinding

class MisContactosFragment : Fragment() {

    private var _binding: FragmentMisContactosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val misNotasViewModel =
            ViewModelProvider(this).get(MisContactosViewModel::class.java)

        _binding = FragmentMisContactosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textContacto
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