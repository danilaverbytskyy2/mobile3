package com.example.app3fragment.viewmodels.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.database.fan.FanUpdateRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class ProgramEditViewModel : ViewModel() {
    fun addProgram(fan: Fan) {
        viewModelScope.launch {
            try {
                RetroBase.RFIT_PROGRAM.addFan(fan)
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun updateProgram(updateRequest: FanUpdateRequest) {
        viewModelScope.launch {
            try {
                RetroBase.RFIT_PROGRAM.updateFan(updateRequest)
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}