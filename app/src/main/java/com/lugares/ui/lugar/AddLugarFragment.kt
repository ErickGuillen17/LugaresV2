package com.lugares.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater,container,false)

        //Se agrega la función para agregar un lugar
        binding.btAdd.setOnClickListener { addLugar() }

        return binding.root
    }

    private fun addLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) {
            val lugar = Lugar(0,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
            lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.lugarAdded),Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        } else {
            Toast.makeText(requireContext(),getString(R.string.noData),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}