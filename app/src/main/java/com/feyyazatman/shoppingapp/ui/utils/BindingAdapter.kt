package com.feyyazatman.shoppingapp.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("productImage")
    fun setProductImage(view:ImageView, url : String) {
        Picasso.get().load(url).into(view)
    }
}