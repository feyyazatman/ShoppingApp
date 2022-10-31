package com.feyyazatman.shoppingapp.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.databinding.ProductItemLayoutBinding

class CategoryAdapter(private val listener: OnProductClickListener)  : ListAdapter<ProductItem, CategoryAdapter.CategoryViewHolder>(PostsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ProductItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position) , listener)
    }

    class CategoryViewHolder(private val binding: ProductItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem, listener: OnProductClickListener) {
            binding.productItem = product
            binding.cardView.setOnClickListener {
                listener.onProductClick(product)
            }
            binding.executePendingBindings()
        }
    }

    class PostsDiffUtil : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnProductClickListener {
    fun onProductClick(product: ProductItem)
}