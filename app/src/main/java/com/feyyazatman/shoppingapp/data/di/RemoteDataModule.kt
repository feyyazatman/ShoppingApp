package com.feyyazatman.shoppingapp.data.di

import com.feyyazatman.shoppingapp.data.remote.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    @Singleton
    fun provideRetrofit(
      converterFactory : GsonConverterFactory,
      baseApiUrl : String
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .addConverterFactory(converterFactory)
            .build()
    }


    @Provides
    @Singleton
    fun provideBaseUrl() : String {
        return Constants.BASE_URL
    }


    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore() = FirebaseFirestore.getInstance()
    
}