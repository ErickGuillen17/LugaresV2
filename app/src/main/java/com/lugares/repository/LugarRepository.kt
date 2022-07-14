package com.lugares.repository

import androidx.lifecycle.MutableLiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    //Se implementan las funciones de la interface

    //Se crea un objeto que contiene el arrayListo de los registros de la tabla lugar... cubiertos por LiveData
    val getAllData: MutableLiveData<List<Lugar>> = lugarDao.getAllData()

    //Se define la función para insertar o actualizar un Lugar en la colleción  misLugares
    fun saveLugar(lugar:Lugar) {
        lugarDao.saveLugar(lugar)
    }

    //Se define la función para eliminar un Lugar en la tabla lugar
    fun deleteLugar(lugar:Lugar) {
        lugarDao.deleteLugar(lugar)
    }
}