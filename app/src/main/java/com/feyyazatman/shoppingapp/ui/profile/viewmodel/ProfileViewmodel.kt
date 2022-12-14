package com.feyyazatman.shoppingapp.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.model.User
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.repository.authentication.AuthRepository
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val basketRepository: BasketRepository
) : ViewModel() {

    private val _subTotal = MutableStateFlow<Double?>(0.0)
    val subTotal: StateFlow<Double?> = _subTotal


    private val _uiState = MutableStateFlow<Resource<User>?>(null)
    val uiState: StateFlow<Resource<User>?> = _uiState

    private val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        println("profile viewmodel calisti")
        currentUser?.let {
            getFirestoreData(it.uid)
        }
    }

    private fun getFirestoreData(uuid : String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = Resource.Loading
            val result = authRepository.getUserData(uuid)
            _uiState.value = result
        }
    }

     fun getSubTotalPrice() {
        viewModelScope.launch {
            val result = basketRepository.getSubTotal()
            _subTotal.value = result.toDouble()
        }
    }



}