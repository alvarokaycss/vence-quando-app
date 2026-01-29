package com.example.vencequando

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("products")
    fun getProducts(): Call<ProductsResponse>

    @POST("products")
    fun addProduct(@Body request: AddProductRequest): Call<AddProductResponse>

    @PUT("products/{id}")
    fun updateProduct(@Path("id") id: Int, @Body request: UpdateProductRequest): Call<UpdateProductResponse>

    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<DeleteProductResponse>
}
