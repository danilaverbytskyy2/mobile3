package com.example.app3fragment.database.program

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.app3fragment.database.company.Company
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@Entity(
    tableName = "programs",
    foreignKeys = [ForeignKey(
        entity = Company::class,
        parentColumns = ["id"],
        childColumns = ["companyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Program @JsonCreator constructor(
    @JsonProperty("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String = "",
    @JsonProperty("developerPhone") val developerPhone: String = "",
    @JsonProperty("companyId") val companyId: Int
)