package com.example.app3fragment.database.program

data class ProgramUpdateRequest(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val developerPhone: String? = null
)