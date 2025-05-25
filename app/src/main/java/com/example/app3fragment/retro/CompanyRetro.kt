package com.example.app3fragment.retro

import com.example.app3fragment.database.company.Company
import com.example.app3fragment.database.company.CompanyRenameRequest
import com.example.app3fragment.database.sector.SectorRenameRequest
import com.example.app3fragment.database.sector.Sector
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CompanyRetro {
    @GET("companies/by-sector/{sectorId}")
    suspend fun getCompaniesBySector(@Path("sectorId") sectorId: Int): List<Company>

    @POST("companies/add")
    suspend fun addCompany(@Body company: Company): Response<Unit>

    @POST("companies/rem")
    suspend fun removeCompany(@Body company: Company): Response<Unit>

    @POST("companies/ren")
    suspend fun renameCompany(@Body companyRenameRequest: CompanyRenameRequest): Response<Unit>
}