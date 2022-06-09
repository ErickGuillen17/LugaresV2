package com.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDatabase
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData: LiveData<List<Lugar>>

    //Esta es la manera como accedo al repositorio desde el viewModel
    private val repository: LugarRepository

    //Se procede a inicializar los atributos de arriba de esta clase LugarViewModel
    init {
        val lugarDao = LugarDatabase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDao)
        getAllData = repository.getAllData
    }

    //Esta función de alto nivel llama al subproceso de I/O para grabar un registro Lugar
    fun addLugar (lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLugar(lugar)
        }
    }

    //Esta función de alto nivel llama al subproceso de I/O para actualizar un registro Lugar
    fun updateLugar (lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLugar(lugar)
        }
    }

    //Esta función de alto nivel llama al subproceso de I/O para eliminar un registro Lugar
    fun deleteLugar (lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLugar(lugar)
        }
    }
}