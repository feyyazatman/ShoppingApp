package com.feyyazatman.shoppingapp.data.remote.utils

sealed class DataState<T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error<T>(val error: String?) : DataState<T>()
    class Loading<T> : DataState<T>()
}


