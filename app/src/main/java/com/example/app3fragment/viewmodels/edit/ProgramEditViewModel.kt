package com.example.app3fragment.viewmodels.edit

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

class ProgramEditViewModel : ViewModel() {
    fun addProgram(program: Program) {
        viewModelScope.launch {
            try {
                RetroBase.RFIT_PROGRAM.addProgram(program)
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun updateProgram(updateRequest: ProgramUpdateRequest) {
        viewModelScope.launch {
            try {
                RetroBase.RFIT_PROGRAM.updateProgram(updateRequest)
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}