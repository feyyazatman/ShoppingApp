package com.feyyazatman.shoppingapp.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import com.feyyazatman.shoppingapp.data.repository.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewmodel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val basketRepository: BasketRepository
) :
    ViewModel() {

    private val _searchState = MutableStateFlow(
        SearchDataState(
            filteredData = mutableListOf(),
            selectedCategories = mutableListOf()
        )
    )
    val searchState: StateFlow<SearchDataState> = _searchState


    private val _subTotal = MutableStateFlow<Double?>(0.0)
    val subTotal: StateFlow<Double?> = _subTotal


    init {
        println("search viewmodel calisti")
        getAllProducts()
        getAllCategories()
    }


    fun searchProduct(query: String) {
        viewModelScope.launch {
            val updateQuery = query.lowercase(Locale.getDefault())

            val currentData = _searchState.value.products
            if (updateQuery != "") {
                currentData?.let { productItems ->
                    val filteredList = productItems.filter {
                        it.title?.lowercase(Locale.getDefault())?.contains(updateQuery) ?: false
                    }
                    _searchState.value =
                        _searchState.value.copy(filteredData = filteredList.toMutableList())
                }
            } else {
                _searchState.value = _searchState.value.copy(filteredData = mutableListOf())
            }
        }
    }

    fun searchProductByCategory(query: String) {
        val updateQuery = query.lowercase(Locale.getDefault())
        val currentData = _searchState.value.products
        if (updateQuery != "") {
            currentData?.let { productItems ->
                val filteredList = productItems.filter {
                    it.category?.lowercase(Locale.getDefault()) == query
                }
                _searchState.value =
                    _searchState.value.copy(filteredData = filteredList.toMutableList())
            }
        } else {
            _searchState.value = _searchState.value.copy(filteredData = mutableListOf())
        }
    }


    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.getAllProducts().enqueue(object : Callback<List<ProductItem>> {
                override fun onResponse(
                    call: Call<List<ProductItem>>,
                    response: Response<List<ProductItem>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val productsList = it
                            _searchState.value = _searchState.value.copy(products = productsList)
                        }
                    }
                }

                override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                    _searchState.value = _searchState.value.copy(error = t.message.toString())
                }

            })
        }
    }


    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.getAllCategories().enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { stringList ->
                            _searchState.update { it.copy(categories = stringList) }
                        }
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    _searchState.value = _searchState.value.copy(error = t.message.toString())
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

data class SearchDataState(
    val products: List<ProductItem>? = null,
    val categories: List<String>? = null,
    val filteredData: MutableList<ProductItem>,
    val selectedCategories: MutableList<String>,
    val error: String? = null
)