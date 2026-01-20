package com.example.vencequando

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val category: String?,
    @SerializedName("expiration_date")
    val expirationDate: String,
    val status: String
)