package com.feyyazatman.shoppingapp.data.repository.category

import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.remote.api.ApiService
import retrofit2.Call

class CategoryRepositoryImpl constructor(
    private val apiService: ApiService
) : CategoryRepository {

    override suspend fun getAllProducts(): Call<List<ProductItem>> {
        return apiService.getAllProducts()
    }

    override suspend fun getAllCategories(): Call<List<String>> {
        return apiService.getAllCategories()
    }
}