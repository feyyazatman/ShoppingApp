package com.feyyazatman.shoppingapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.feyyazatman.shoppingapp.databinding.SearchCategoryItemLayoutBinding

class SearchCategoryAdapter(private val listener: OnFilteredCategoryClickListener)  : ListAdapter<String, SearchCategoryAdapter.SearchViewHolder>(SearchCategoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchCategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position) , listener)
    }

    class SearchViewHolder(private val binding: SearchCategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String, listener: OnFilteredCategoryClickListener) {
            binding.tvCategory.text = category
            binding.cardViewCategory.setOnClickListener {
                listener.onFilteredCategoryClick(category)
            }
        }
    }

    class SearchCategoryDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnFilteredCategoryClickListener {
    fun onFilteredCategoryClick(category: String)
}