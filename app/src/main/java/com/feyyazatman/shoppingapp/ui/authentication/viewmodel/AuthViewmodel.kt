package com.feyyazatman.shoppingapp.ui.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.repository.authentication.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginState: StateFlow<Resource<FirebaseUser>?> = _loginState


    private val _signUpState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpState: StateFlow<Resource<FirebaseUser>?> = _signUpState

    private val _uiEvent = MutableSharedFlow<RegisterViewEvent>(replay = 0)
    val uiEvent: SharedFlow<RegisterViewEvent> = _uiEvent



    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isValidFields(email, password)) {
                _loginState.value = Resource.Loading
                val result = authRepository.signIn(email, password)
                _loginState.value = result
            } else {
                _loginState.emit(Resource.Failure(exception = "Please fill all fields"))
            }
        }
    }

    fun signUp(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
          isValidFieldsForRegister(username, email, password, confirmPassword)?.let {
              _uiEvent.emit(RegisterViewEvent.ShowError(it))
          } ?: kotlin.run {
              _signUpState.value = Resource.Loading
              val result = authRepository.signUp(username, email, password)
              _signUpState.value = result
            }
        }
    }

    private fun isValidFields(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }


    private fun isValidFieldsForRegister(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        fun isValidEmptyField() =
            email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty()

        fun isValidConfirmPassword() = password == confirmPassword
        fun isValidPasswordLength() = password.length >= 6
        fun isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (isValidEmptyField().not()) {
            return "Please fill all fields"
        } else if (isValidEmail().not()) {
            return "Please enter a valid email address"
        } else if (isValidConfirmPassword().not()) {
            return "Passwords do not match"
        } else if (isValidPasswordLength().not()) {
            return "Password must be at least 6 characters"
        }
        return null

    }

    fun logout() {
        authRepository.logOut()
        _loginState.value = null
        _signUpState.value = null
    }

}

sealed class RegisterViewEvent {
    class ShowError(val error: String) : RegisterViewEvent()
}
