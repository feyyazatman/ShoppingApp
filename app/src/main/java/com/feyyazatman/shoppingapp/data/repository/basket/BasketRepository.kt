package com.feyyazatman.shoppingapp.data.repository.basket

import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.google.firebase.auth.FirebaseUser


interface BasketRepository {
    val currentUser: FirebaseUser?
    suspend fun setBasketData(basketProductItem: BasketProductItem)
    suspend fun getBasketData() : Resource<List<BasketProductItem>>
    suspend fun incrementBasketData(basketProductItem: BasketProductItem)
    suspend fun deleteBasketData()
    suspend fun decrementBasketData(basketProductItem: BasketProductItem)
    suspend fun addSubTotal(price : Double)
    suspend fun getSubTotal() : Number
}