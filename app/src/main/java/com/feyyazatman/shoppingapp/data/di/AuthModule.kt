package com.feyyazatman.shoppingapp.data.di

import com.feyyazatman.shoppingapp.data.repository.authentication.AuthRepository
import com.feyyazatman.shoppingapp.data.repository.authentication.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AuthModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) : AuthRepository{
        return AuthRepositoryImpl(firebaseAuth,firestore)
    }
}