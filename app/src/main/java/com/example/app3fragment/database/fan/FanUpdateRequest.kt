package com.example.app3fragment.database.fan

data class FanUpdateRequest(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val developerPhone: String? = null
)