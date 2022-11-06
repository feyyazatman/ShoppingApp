package com.feyyazatman.shoppingapp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.repository.authentication.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<MainEvent>(MainEvent.toEmpty)
    val authState: StateFlow<MainEvent> = _authState

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        viewModelScope.launch {
            if (currentUser != null) {
                _authState.emit(MainEvent.toCategory)
            } else _authState.emit(MainEvent.toAuth)
        }
    }

}


sealed class MainEvent{
    object toAuth : MainEvent()
    object toCategory : MainEvent()
    object toEmpty : MainEvent()
}