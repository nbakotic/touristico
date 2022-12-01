package com.example.touristico.guest.models

data class GuestBook(
    val negative: String ? = "",
    val positive: String ? = "",
    val name: String ? = "",
    val country: String ? = "",
    val stars: Float = 0.0f,
    val time: String = ""
)