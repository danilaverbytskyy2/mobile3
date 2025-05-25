package com.example.app3fragment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3fragment.database.company.Company
import com.example.app3fragment.database.company.CompanyRenameRequest
import com.example.app3fragment.database.sector.Sector
import com.example.app3fragment.database.sector.SectorRenameRequest
import com.example.app3fragment.retro.RetroBase
import kotlinx.coroutines.launch

class CompanyViewModel(private val sectorId: Int) : ViewModel() {
    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>> = _companies

    init {
        viewModelScope.launch {
            loadDataFromServer()
        }
    }

    private suspend fun loadDataFromServer() {
        try {
            val serverCompanies = RetroBase.RFIT_COMPANY.getCompaniesBySector(this.sectorId)
            _companies.postValue(serverCompanies)
        } catch (e: Exception) {
            e.message?.let { Log.e("Err", it) }
        }
    }

    fun renameCompany(company: Company, newName: String) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.renameCompany(CompanyRenameRequest(company.id, newName))
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun addCompany(company: Company) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.addCompany(company)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }

    fun removeCompany(company: Company) {
        viewModelScope.launch {
            try {
                val response = RetroBase.RFIT_COMPANY.removeCompany(company)
                if (response.isSuccessful) {
                    loadDataFromServer()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Err", it) }
            }
        }
    }
}