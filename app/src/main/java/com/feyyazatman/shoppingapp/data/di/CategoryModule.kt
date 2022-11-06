package com.feyyazatman.shoppingapp.data.di

import com.feyyazatman.shoppingapp.data.remote.api.ApiService
import com.feyyazatman.shoppingapp.data.repository.category.CategoryRepositoryImpl
import com.feyyazatman.shoppingapp.data.repository.category.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
class CategoryModule {

    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideCategoryRepository(apiService: ApiService) : CategoryRepository {
        return CategoryRepositoryImpl(apiService)
    }
}