package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.label.Label
import com.example.app3fragment.database.label.LebelRenameRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class LabelViewModel() : ViewModel() {
    private val _labels = MutableLiveData<List<Label>>()
    val labels: LiveData<List<Label>> = _labels

    init {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverLabels = RetroBase.RFIT_SECTOR.getLabels()
            _labels.postValue(serverLabels)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun renameLabel(label: Label, newName: String) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.renameLabel(LebelRenameRequest(label.id, newName))
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun addLabel(label: Label) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.addLabel(label)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun removeLabel(label: Label) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_SECTOR.removeLabel(label)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}