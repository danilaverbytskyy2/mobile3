package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.program.Program
import com.example.app3fragment.database.program.ProgramUpdateRequest
import com.example.app3fragment.database.sector.Sector
import com.example.app3fragment.database.sector.SectorRenameRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class ProgramViewModel(private val companyId: Int) : ViewModel() {
    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>> = _programs

    public fun fetch() {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverPrograms = RetroBase.RFIT_PROGRAM.getProgramsByCompany(companyId)
            _programs.postValue(serverPrograms)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun removeProgram(program: Program) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_PROGRAM.removeProgram(program)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}