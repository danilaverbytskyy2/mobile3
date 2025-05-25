package com.example.app3fragment.database.artist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.app3fragment.database.label.Label
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@Entity(tableName = "artists",
    foreignKeys = [ForeignKey(
        entity = Label::class,
        parentColumns = ["id"],
        childColumns = ["labelId"],
        onDelete = ForeignKey.CASCADE)
    ])
data class Artist @JsonCreator constructor(
    @JsonProperty("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @JsonProperty("name") val name: String,
    @JsonProperty("labelId") val labelId: Int,
)