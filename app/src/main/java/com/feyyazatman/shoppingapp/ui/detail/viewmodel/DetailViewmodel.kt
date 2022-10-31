package com.feyyazatman.shoppingapp.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewmodel : ViewModel() {

    private val _amount =  MutableStateFlow(1)
    val amount: StateFlow<Int> = _amount


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