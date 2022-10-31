package com.feyyazatman.shoppingapp.data.remote.api

import com.feyyazatman.shoppingapp.data.model.ProductItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products")
    fun getAllProducts() : Call<List<ProductItem>>

    @GET("product/{id}")
    fun getProductById(@Path("{id}") id: String) : Call<ProductItem>
}