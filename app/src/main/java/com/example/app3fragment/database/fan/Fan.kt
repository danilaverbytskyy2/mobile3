package com.example.app3fragment.database.fan

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.app3fragment.database.artist.Artist
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@Entity(
    tableName = "fans",
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"],
        childColumns = ["artistId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Fan @JsonCreator constructor(
    @JsonProperty("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String = "",
    @JsonProperty("developerPhone") val developerPhone: String = "",
    @JsonProperty("artistId") val artistId: Int
)