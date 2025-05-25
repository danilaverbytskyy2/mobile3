package com.example.app3fragment.database.program

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ProgramDAO {
    @Insert
    suspend fun insert(program: Program)

    @Delete
    suspend fun delete(program: Program)

    @Update
    suspend fun update(program: Program)

    @Query("UPDATE programs SET name = :newName WHERE id = :programId")
    suspend fun updateName(programId: Int, newName: String)

    @Query("UPDATE programs SET description = :newDescription WHERE id = :programId")
    suspend fun updateDescription(programId: Int, newDescription: String)

    @Query("UPDATE programs SET developerPhone = :newPhone WHERE id = :programId")
    suspend fun updateDeveloperPhone(programId: Int, newPhone: String)

    @Query("SELECT * FROM programs WHERE companyId = :companyId")
    suspend fun getProgramsByCompany(companyId: Int): List<Program>

    @Query("SELECT * FROM programs WHERE id = :programId")
    suspend fun getProgramById(programId: Int): Program?
}