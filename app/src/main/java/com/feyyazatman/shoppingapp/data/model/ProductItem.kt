package com.feyyazatman.shoppingapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductItem(
    @SerializedName("category")
    val category: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("rating")
    val rating: Rating?,
    @SerializedName("title")
    val title: String?
) : Parcelable



data class BasketProductItem(
    val category: String?,
    val description: String?,
    val id: String?,
    val image: String?,
    val price: Double,
    val title: String?,
    val amount : Long?, // try
    val subTotal: Double = 0.0,
)