package com.example.app3fragment.database.sector

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SectorDAO {
    @Query("SELECT * FROM sectors")
    suspend fun getAll(): List<Sector>

    @Insert
    suspend fun insert(sector: Sector)

    @Delete
    suspend fun delete(sector: Sector)

    @Query("UPDATE sectors SET name = :newName WHERE id = :sectorId")
    suspend fun updateName(sectorId: Int, newName: String)
}