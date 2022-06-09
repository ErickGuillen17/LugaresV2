package com.lugares.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentLugarBinding.inflate(inflater,container,false)

        //Se programa la acción para pasarse a AddLugar
        binding.addLugarButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}