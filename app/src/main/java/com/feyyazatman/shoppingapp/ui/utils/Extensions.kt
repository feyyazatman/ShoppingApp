package com.feyyazatman.shoppingapp.ui.utils

object Extensions {

    fun Double.format(scale: Int) = "%.${scale}f".format(this)
}