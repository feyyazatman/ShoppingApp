package com.feyyazatman.shoppingapp.data.repository.authentication

import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signIn(email:String, password: String) : Resource<FirebaseUser>
    suspend fun signUp(username:String,email: String,password: String) : Resource<FirebaseUser>
    fun logOut()
}