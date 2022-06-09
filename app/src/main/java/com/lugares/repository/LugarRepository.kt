package com.lugares.repository

import androidx.lifecycle.LiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    //Se implementan las funciones de la interface

    //Se crea un objeto que contiene el arrayListo de los registros de la tabla lugar... cubiertos por LiveData
    val getAllData: LiveData<List<Lugar>> = lugarDao.getAllData()

    //Se define la función para insertar un Lugar en la tabla lugar
    suspend fun addLugar(lugar:Lugar) {
        lugarDao.addLugar(lugar)
    }

    //Se define la función para actualizar un Lugar en la tabla lugar
    suspend fun updateLugar(lugar:Lugar) {
        lugarDao.updateLugar(lugar)
    }

    //Se define la función para eliminar un Lugar en la tabla lugar
    suspend fun deleteLugar(lugar:Lugar) {
        lugarDao.deleteLugar(lugar)
    }
}