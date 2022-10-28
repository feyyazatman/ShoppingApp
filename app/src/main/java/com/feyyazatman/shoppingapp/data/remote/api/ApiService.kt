package com.feyyazatman.shoppingapp.data.remote.api

import com.feyyazatman.shoppingapp.data.model.Product
import com.feyyazatman.shoppingapp.data.model.ProductItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products")
    suspend fun getAllProducts() : Response<Product>

    @GET("product/{id}")
    suspend fun getProductById(@Path("{id}") id: String) : Response<ProductItem>
}