package com.example.app3fragment.retro

import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.database.fan.FanUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FanRetro {
    @GET("fans/by-fan/{fanId}")
    suspend fun getFanById(@Path("fanId") fanId: Int): Fan

    @GET("fans/by-artist/{artistId}")
    suspend fun getFansByArtist(@Path("artistId") artistId: Int): List<Fan>

    @POST("fans/add")
    suspend fun addFan(@Body fan: Fan): Response<Unit>

    @POST("fans/rem")
    suspend fun removeFan(@Body fan: Fan): Response<Unit>

    @POST("fans/update")
    suspend fun updateFan(@Body fanUpdateRequest: FanUpdateRequest): Response<Unit>
}