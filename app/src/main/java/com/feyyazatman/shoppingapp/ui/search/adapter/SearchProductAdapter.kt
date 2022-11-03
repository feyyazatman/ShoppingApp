package com.feyyazatman.shoppingapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.databinding.SearchItemLayoutBinding

class SearchProductAdapter(private val listener: OnFilteredProductClickListener)  : ListAdapter<ProductItem, SearchProductAdapter.SearchViewHolder>(SearchDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position) , listener)
    }

    class SearchViewHolder(private val binding: SearchItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductItem, listener: OnFilteredProductClickListener) {
            binding.productItem = product
            binding.cardView.setOnClickListener {
                listener.onFilteredProductClick(product)
            }
            binding.executePendingBindings()
        }
    }

    class SearchDiffUtil : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnFilteredProductClickListener {
    fun onFilteredProductClick(product: ProductItem)
}
