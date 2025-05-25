package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.artist.Artist
import com.example.app3fragment.database.artist.ArtistRenameRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class ArtistViewModel(private val artistId: Int) : ViewModel() {
    private val _companies = MutableLiveData<List<Artist>>()
    val companies: LiveData<List<Artist>> = _companies

    init {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverCompanies = RetroBase.RFIT_COMPANY.getArtistsByLabel(this.artistId)
            _companies.postValue(serverCompanies)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun renameArtist(artist: Artist, newName: String) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.renameArtist(ArtistRenameRequest(artist.id, newName))
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun addArtist(artist: Artist) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.addArtist(artist)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun removeArtist(artist: Artist) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.removeArtist(artist)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}