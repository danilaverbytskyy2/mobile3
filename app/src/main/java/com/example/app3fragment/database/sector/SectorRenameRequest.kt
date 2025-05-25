package com.example.app3fragment.database.sector

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SectorRenameRequest(
    val id: Int,
    val newName: String
)