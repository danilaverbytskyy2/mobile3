package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.sector.Sector
import com.example.app3fragment.database.sector.SectorRenameRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class SectorViewModel() : ViewModel() {
    private val _sectors = MutableLiveData<List<Sector>>()
    val sectors: LiveData<List<Sector>> = _sectors

    init {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverSectors = RetroBase.RFIT_SECTOR.getSectors()
            _sectors.postValue(serverSectors)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun renameSector(sector: Sector, newName: String) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.renameSector(SectorRenameRequest(sector.id, newName))
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun addSector(sector: Sector) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.addSector(sector)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun removeSector(sector: Sector) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.removeSector(sector)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}