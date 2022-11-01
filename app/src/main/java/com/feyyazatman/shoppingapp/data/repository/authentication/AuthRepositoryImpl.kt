package com.feyyazatman.shoppingapp.data.repository.authentication

import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth,private val fireStore: FirebaseFirestore) : AuthRepository{
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(result.user!!)
        } catch (e:Exception) {
            e.printStackTrace()
            Resource.Failure(e.message)
        }
    }

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await().apply {
                fireStore.collection("users").add(mapOf("username" to username, "uuid" to this.user?.uid)).await()
            }
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())?.await()
            Resource.Success(result.user!!)
        } catch (e:Exception) {
            e.printStackTrace()
            Resource.Failure(e.message)
        }
    }


    override fun logOut() {
        firebaseAuth.signOut()
    }
}