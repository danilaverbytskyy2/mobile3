package com.example.app3fragment.retro

import com.example.app3fragment.database.label.LebelRenameRequest
import com.example.app3fragment.database.label.Label
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LabelRetro {
    @GET("labels")
    suspend fun getLabels(): List<Label>

    @POST("labels/add")
    suspend fun addLabel(@Body label: Label): Response<Unit>

    @POST("labels/rem")
    suspend fun removeLabel(@Body label: Label): Response<Unit>

    @POST("labels/ren")
    suspend fun renameLabel(@Body lebelRenameRequest: LebelRenameRequest): Response<Unit>
}