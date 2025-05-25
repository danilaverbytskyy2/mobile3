package com.example.app3fragment.retro

import com.example.app3fragment.database.program.Program
import com.example.app3fragment.database.program.ProgramUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProgramRetro {
    @GET("programs/by-prog/{programId}")
    suspend fun getProgramById(@Path("programId") programId: Int): Program

    @GET("programs/by-company/{companyId}")
    suspend fun getProgramsByCompany(@Path("companyId") companyId: Int): List<Program>

    @POST("programs/add")
    suspend fun addProgram(@Body program: Program): Response<Unit>

    @POST("programs/rem")
    suspend fun removeProgram(@Body program: Program): Response<Unit>

    @POST("programs/update")
    suspend fun updateProgram(@Body programUpdateRequest: ProgramUpdateRequest): Response<Unit>
}