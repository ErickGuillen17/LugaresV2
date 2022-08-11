package com.lugares.ui.gallery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lugares.databinding.FragmentGalleryBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.GalleryViewModel
import com.lugares.viewmodel.LugarViewModel

class GalleryFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    //El objeto para interactuar con la vista (mapa)
    private lateinit var googleMap: GoogleMap
    private var mapReady = false

    private lateinit var lugarViewModel: LugarViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //se solicita la acutalización del mapa...
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
            mapReady = true
            //se instruye al mapa para que se vean los lugares... segun coordenadas
            lugarViewModel.getAllData.observe(viewLifecycleOwner) {
                lugares ->
                updateMap(lugares)
                ubicaCamara()
            }
        }
    }

    private fun ubicaCamara() {
        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {  //Si no tengo los permisos... entonces pido los permisos
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),105)
        } else {  //Tengo los permisos entonces recupero las coordenadas...
            ubicacion.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location!=null) { //Se pudo leer las coordenadas gps...
                        val camaraUpdate = CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude,location.longitude),15f)
                        googleMap.animateCamera(camaraUpdate)
                    }
                }
        }
    }

    private fun updateMap(lugares: List<Lugar>) {
        if (mapReady) {
            lugares.forEach { lugar ->
                if (lugar.latitud?.isFinite() == true && lugar.longitud?.isFinite() == true) {
                    val marca = LatLng(lugar.latitud,lugar.longitud)
                    googleMap.addMarker(MarkerOptions().position(marca).title(lugar.nombre))
                }
            }
        }
    }
}