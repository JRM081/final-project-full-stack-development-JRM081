package com.example.greenspot

import java.util.Date
import java.util.UUID

data class Plant(
    val id: UUID,
    val title: String,
    val date: Date,
    val place: String
)
