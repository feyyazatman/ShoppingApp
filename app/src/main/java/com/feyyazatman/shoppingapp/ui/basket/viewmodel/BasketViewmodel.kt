package com.feyyazatman.shoppingapp.ui.basket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewmodel @Inject constructor(private val basketRepository: BasketRepository) :
    ViewModel() {

    private val _amount = MutableStateFlow(1)
    val amount: StateFlow<Int> = _amount

    private val _subTotal = MutableStateFlow(0.0)
    val subTotal: StateFlow<Double> = _subTotal

    private val _uiState = MutableStateFlow<Resource<List<BasketProductItem>>?>(null)
    val uiState: StateFlow<Resource<List<BasketProductItem>>?> = _uiState


    init {
        getToCart()
        viewModelScope.launch {
            val result = basketRepository.getBasketData()
            if (result is Resource.Success) {
                result.result.forEach {
                    getSubTotal(it.subTotal)
                }
            }
        }
    }

    private fun getToCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = basketRepository.getBasketData()
            _uiState.value = result
        }
    }

    fun deleteAllBasketData() {
        viewModelScope.launch(Dispatchers.IO) {
            basketRepository.deleteBasketData()
            getToCart()
        }
    }

    fun getSubTotal(price : Double) {
        _subTotal.value = price + _subTotal.value
    }


    fun incraseAmount(basketProductItem: BasketProductItem) {
        viewModelScope.launch {
            _amount.value += 1
            basketRepository.incrementBasketData(basketProductItem)
            _subTotal.value = basketProductItem.price + _subTotal.value
            getToCart()
        }

    }


    fun decreaseAmount(basketProductItem: BasketProductItem) {
        viewModelScope.launch {
            _amount.value -= 1
            basketRepository.decrementBasketData(basketProductItem)
            _subTotal.value = _subTotal.value - basketProductItem.price
            getToCart()
        }
    }
}