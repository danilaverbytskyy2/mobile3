package com.example.app3fragment.database.label

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LabelDAO {
    @Query("SELECT * FROM labels")
    suspend fun getAll(): List<Label>

    @Insert
    suspend fun insert(label: Label)

    @Delete
    suspend fun delete(label: Label)

    @Query("UPDATE labels SET name = :newName WHERE id = :labelId")
    suspend fun updateName(labelId: Int, newName: String)
}