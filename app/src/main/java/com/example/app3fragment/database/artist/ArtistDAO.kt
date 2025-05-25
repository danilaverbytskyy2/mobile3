package com.example.app3fragment.database.artist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArtistDAO {
    @Insert
    suspend fun insert(artist: Artist)

    @Delete
    suspend fun delete(artist: Artist)

    @Query("UPDATE artists SET name = :newName WHERE id = :artistId")
    suspend fun updateName(artistId: Int, newName: String)

    @Query("SELECT * FROM artists WHERE labelId = :labelId")
    suspend fun getArtistsByLabel(labelId: Int): List<Artist>

    @Query("SELECT * FROM artists WHERE id = :artistId")
    suspend fun getArtistById(artistId: Int): Artist?
}