package com.example.memoriavivaapp.ui.mi_cuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentMiCuentaBinding

class MiCuentaFragment : Fragment() {

    private var _binding: FragmentMiCuentaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val miCuentaViewModel =
            ViewModelProvider(this).get(MiCuentaViewModel::class.java)

        _binding = FragmentMiCuentaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCuenta
        miCuentaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}