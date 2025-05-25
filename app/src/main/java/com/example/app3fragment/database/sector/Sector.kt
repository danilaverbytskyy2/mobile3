package com.example.app3fragment.database.sector

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


@Entity(tableName = "sectors")
data class Sector @JsonCreator constructor(
    @JsonProperty("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @JsonProperty("name") val name: String)
