package com.example.app3fragment.database.company

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.app3fragment.database.sector.Sector
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@Entity(tableName = "companies",
    foreignKeys = [ForeignKey(
        entity = Sector::class,
        parentColumns = ["id"],
        childColumns = ["sectorId"],
        onDelete = ForeignKey.CASCADE)
    ])
data class Company @JsonCreator constructor(
    @JsonProperty("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @JsonProperty("name") val name: String,
    @JsonProperty("sectorId") val sectorId: Int,
)