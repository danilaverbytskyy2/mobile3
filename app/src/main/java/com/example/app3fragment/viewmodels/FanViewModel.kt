package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class FanViewModel(private val companyId: Int) : ViewModel() {
    private val _programs = MutableLiveData<List<Fan>>()
    val programs: LiveData<List<Fan>> = _programs

    public fun fetch() {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverPrograms = RetroBase.RFIT_PROGRAM.getFansByArtist(companyId)
            _programs.postValue(serverPrograms)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun removeFan(fan: Fan) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_PROGRAM.removeFan(fan)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}