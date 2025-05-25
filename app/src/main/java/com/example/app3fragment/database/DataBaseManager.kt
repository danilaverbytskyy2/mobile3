package com.example.app3fragment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app3fragment.database.company.Company
import com.example.app3fragment.database.company.CompanyDAO
import com.example.app3fragment.database.program.Program
import com.example.app3fragment.database.program.ProgramDAO
import com.example.app3fragment.database.sector.Sector
import com.example.app3fragment.database.sector.SectorDAO

@Database(entities = [Sector::class, Company::class, Program::class], version = 3, exportSchema = false)
abstract class DataBaseManager : RoomDatabase() {
    abstract fun sectorDao(): SectorDAO
    abstract fun companyDao(): CompanyDAO
    abstract fun programDao(): ProgramDAO
}