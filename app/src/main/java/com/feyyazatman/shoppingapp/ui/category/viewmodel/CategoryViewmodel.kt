package com.feyyazatman.shoppingapp.ui.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import com.feyyazatman.shoppingapp.data.repository.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryViewmodel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val basketRepository: BasketRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    private val _subTotal = MutableStateFlow<Double?>(0.0)
    val subTotal: StateFlow<Double?> = _subTotal


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ProductUiState(isLoading = true)
            categoryRepository.getAllProducts().enqueue(object : Callback<List<ProductItem>> {
                override fun onResponse(
                    call: Call<List<ProductItem>>,
                    response: Response<List<ProductItem>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val updateProductList = it
                            _uiState.value =
                                _uiState.value.copy(isLoading = false, products = updateProductList)
                        } ?: kotlin.run {
                            _uiState.value = uiState.value.copy(error = response.message())
                        }
                    } else _uiState.value = uiState.value.copy(error = response.message())
                }

                override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                    _uiState.value = _uiState.value.copy(error = t.message.toString())
                }

            })
        }
    }

     fun getSubTotalPrice() {
        viewModelScope.launch {
            val result = basketRepository.getSubTotal()
            _subTotal.value = result.toDouble()
        }
    }
}

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<ProductItem>? = null,
    val error: String? = null
)

