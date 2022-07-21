package com.lugares.ui.lugar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.model.Lugar
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var audioUtiles: AudioUtiles
    private lateinit var imagenUtiles : ImagenUtiles
    private lateinit var tomarFotoActivity : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentAddLugarBinding.inflate(inflater,container,false)

        //Se agrega la función para agregar un lugar
        binding.btAdd.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subeAudio()
        }

        audioUtiles = AudioUtiles(requireActivity(),
        requireContext(),
        binding.btAccion,
        binding.btPlay,
        binding.btDelete,
        getString(R.string.msg_graba_audio),
        getString(R.string.msg_detener_audio))

        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles(
            requireContext(),
            binding.btPhoto,
            binding.btRotaL,
            binding.btRotaR,
            binding.imagen,
            tomarFotoActivity)

        return binding.root
    }

    private fun subeAudio() {
        val audioFile = audioUtiles.audioFile
        if (audioFile.exists() && audioFile.isFile && audioFile.canRead()) {
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia : StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subeImagen(rutaAudio)
                        }
                }
                .addOnFailureListener {
                    subeImagen("")
                }
        } else {
            subeImagen("")
        }
    }

    private fun subeImagen(rutaAudio: String) {
        binding.msgMensaje.text = getString(R.string.msg_subiendo_imagen)
        val imagenFile = imagenUtiles.imagenFile
        if (imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()) {
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"
            val referencia : StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            addLugar(rutaAudio,rutaImagen)
                        }
                }
                .addOnFailureListener {
                    addLugar(rutaAudio, "")
                }
        } else {
            addLugar(rutaAudio,"")
        }
    }

    private fun addLugar(rutaAudio: String,rutaImagen: String) {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) {
            val lugar = Lugar("",nombre,correo,telefono,web,0.0,0.0,0.0,rutaAudio,rutaImagen)
            lugarViewModel.saveLugar(lugar)
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