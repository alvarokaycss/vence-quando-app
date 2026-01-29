package com.example.vencequando

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserData?
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)

data class ProductsResponse(
    val products: List<Product>
)

data class AddProductRequest(
    val name: String,
    val category: String?,
    val expiration_date: String,
)

data class AddProductResponse(
    val message: String
)

data class UpdateProductRequest(
    val name: String,
    val category: String?,
    val expiration_date: String
)

data class UpdateProductResponse(
    val message: String
)

data class DeleteProductResponse(
    val message: String
)
