package com.feyyazatman.shoppingapp.ui.basket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.databinding.BottomSheetItemLayoutBinding


class BottomSheetAdapter(private val listener: OnBasketItemClickListener) :
    ListAdapter<BasketProductItem, BottomSheetAdapter.BasketViewHolder>(BasketViewHolder.PostsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(
            BottomSheetItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class BasketViewHolder(private val binding: BottomSheetItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: BasketProductItem, listener: OnBasketItemClickListener) {
            binding.productItem = product
            binding.amount = product.amount?.toInt()
            binding.tvAmount.text = (product.subTotal / product.price).toInt().toString()
            binding.ivAdd.setOnClickListener {
                listener.onIncrementClick(product)
            }
            binding.ivRemove.setOnClickListener {
                listener.onDecrementClick(product)
            }
            binding.executePendingBindings()
        }

        class PostsDiffUtil : DiffUtil.ItemCallback<BasketProductItem>() {
            override fun areItemsTheSame(
                oldItem: BasketProductItem,
                newItem: BasketProductItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BasketProductItem,
                newItem: BasketProductItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface OnBasketItemClickListener {
    fun onIncrementClick(basketProductItem: BasketProductItem)
    fun onDecrementClick(basketProductItem: BasketProductItem)
}
