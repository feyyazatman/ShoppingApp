package com.feyyazatman.shoppingapp.data.di

import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepository
import com.feyyazatman.shoppingapp.data.repository.basket.BasketRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class BasketModule {

    @Provides
    fun provideBasketRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) : BasketRepository {
        return BasketRepositoryImpl(firebaseAuth,firestore)
    }
}