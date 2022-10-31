package com.feyyazatman.shoppingapp.data.repository.Category

import com.feyyazatman.shoppingapp.data.model.ProductItem
import retrofit2.Call

interface CategoryReposityory {

    suspend fun getAllProducts() : Call<List<ProductItem>>
}