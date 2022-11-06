package com.feyyazatman.shoppingapp.data.repository.category

import com.feyyazatman.shoppingapp.data.model.ProductItem
import retrofit2.Call

interface CategoryRepository {

    suspend fun getAllProducts() : Call<List<ProductItem>>

    suspend fun getAllCategories() : Call<List<String>>
}