package com.example.memoriavivaapp.ui.mi_familia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.databinding.FragmentMiFamiliaBinding

class MiFamiliaFragment : Fragment() {

    private var _binding: FragmentMiFamiliaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val miFamiliaViewModel =
            ViewModelProvider(this).get(MiFamiliaViewModel::class.java)

        _binding = FragmentMiFamiliaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFamilia
        miFamiliaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}