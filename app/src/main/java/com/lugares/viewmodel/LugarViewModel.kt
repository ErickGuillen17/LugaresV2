package com.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDao
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData: MutableLiveData<List<Lugar>>

    //Esta es la manera como accedo al repositorio desde el viewModel
    private val repository: LugarRepository = LugarRepository(LugarDao())

    //Se procede a inicializar los atributos de arriba de esta clase LugarViewModel
    init { getAllData = repository.getAllData }

    //Esta función de alto nivel llama al subproceso de I/O para grabar o actualizar un Lugar
    fun saveLugar (lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLugar(lugar)
        }
    }

    //Esta función de alto nivel llama al subproceso de I/O para eliminar un registro Lugar
    fun deleteLugar (lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLugar(lugar)
        }
    }
}