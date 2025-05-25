package com.example.app3fragment.retro

import com.example.app3fragment.database.sector.SectorRenameRequest
import com.example.app3fragment.database.sector.Sector
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SectorRetro {
    @GET("sectors")
    suspend fun getSectors(): List<Sector>

    @POST("sectors/add")
    suspend fun addSector(@Body sector: Sector): Response<Unit>

    @POST("sectors/rem")
    suspend fun removeSector(@Body sector: Sector): Response<Unit>

    @POST("sectors/ren")
    suspend fun renameSector(@Body sectorRenameRequest: SectorRenameRequest): Response<Unit>
}