package com.example.app3fragment.database.company

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CompanyDAO {
    @Insert
    suspend fun insert(company: Company)

    @Delete
    suspend fun delete(company: Company)

    @Query("UPDATE companies SET name = :newName WHERE id = :companyId")
    suspend fun updateName(companyId: Int, newName: String)

    @Query("SELECT * FROM companies WHERE sectorId = :sectorId")
    suspend fun getCompaniesBySector(sectorId: Int): List<Company>

    @Query("SELECT * FROM companies WHERE id = :companyId")
    suspend fun getCompanyById(companyId: Int): Company?
}