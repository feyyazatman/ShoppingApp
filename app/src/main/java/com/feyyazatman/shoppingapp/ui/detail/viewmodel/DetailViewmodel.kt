package com.feyyazatman.shoppingapp.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewmodel @Inject constructor(private val basketRepository: BasketRepository) :
    ViewModel() {

    private val _amount = MutableStateFlow(1)
    val amount: StateFlow<Int> = _amount

    private val _subTotal = MutableStateFlow<Double?>(0.0)
    val subTotal: StateFlow<Double?> = _subTotal


    private val _uiState = MutableStateFlow<Resource<List<BasketProductItem>>?>(null)
    val uiState: StateFlow<Resource<List<BasketProductItem>>?> = _uiState



    fun addToCart(productItem: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            basketRepository.setBasketData(
                BasketProductItem(
                    category = productItem.category,
                    description = productItem.description,
                    id = productItem.id.toString(),
                    image = productItem.image,
                    price = productItem.price!!.toDouble(),
                    title = productItem.title,
                    amount = _amount.value.toLong(),
                    subTotal = _amount.value.toDouble() * productItem.price
                )
            )
            getSubTotalPrice()
        }

    }


    fun getSubTotalPrice() {
        viewModelScope.launch {
            val result = basketRepository.getSubTotal()
            _subTotal.value = result.toDouble()
        }
    }


    fun incraseAmount() {
        viewModelScope.launch {
            _amount.value += 1
        }
    }


    fun decreaseAmount() {
        viewModelScope.launch {
            if (_amount.value > 1) {
                _amount.value -= 1
            }
        }
    }

}