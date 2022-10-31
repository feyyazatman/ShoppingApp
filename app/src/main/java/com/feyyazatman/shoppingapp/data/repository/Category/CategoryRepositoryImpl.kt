package com.feyyazatman.shoppingapp.data.repository.Category

import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.remote.api.ApiService
import retrofit2.Call

class CategoryRepositoryImpl constructor(
    private val apiService: ApiService
) : CategoryReposityory {

    override suspend fun getAllProducts(): Call<List<ProductItem>> {
        return apiService.getAllProducts()
    }
}