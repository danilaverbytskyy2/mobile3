package com.example.app3fragment.retro

import com.example.app3fragment.database.artist.Artist
import com.example.app3fragment.database.artist.ArtistRenameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArtistRetro {
    @GET("artists/by-label/{labelId}")
    suspend fun getArtistsByLabel(@Path("labelId") labelId: Int): List<Artist>

    @POST("artists/add")
    suspend fun addArtist(@Body artist: Artist): Response<Unit>

    @POST("artists/rem")
    suspend fun removeArtist(@Body artist: Artist): Response<Unit>

    @POST("artists/ren")
    suspend fun renameArtist(@Body artistRenameRequest: ArtistRenameRequest): Response<Unit>
}