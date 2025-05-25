package com.example.app3fragment.database.fan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface FanDAO {
    @Insert
    suspend fun insert(fan: Fan)

    @Delete
    suspend fun delete(fan: Fan)

    @Update
    suspend fun update(fan: Fan)

    @Query("UPDATE fans SET name = :newName WHERE id = :fanId")
    suspend fun updateName(fanId: Int, newName: String)

    @Query("UPDATE fans SET description = :newDescription WHERE id = :fanId")
    suspend fun updateDescription(fanId: Int, newDescription: String)

    @Query("UPDATE fans SET developerPhone = :newPhone WHERE id = :fanId")
    suspend fun updateDeveloperPhone(fanId: Int, newPhone: String)

    @Query("SELECT * FROM fans WHERE artistId = :artistId")
    suspend fun getFansByArtist(artistId: Int): List<Fan>

    @Query("SELECT * FROM fans WHERE id = :fanId")
    suspend fun getFanById(fanId: Int): Fan?
}