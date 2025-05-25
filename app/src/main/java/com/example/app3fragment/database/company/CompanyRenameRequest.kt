package com.example.app3fragment.database.company

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CompanyRenameRequest(
    val id: Int,
    val newName: String
)