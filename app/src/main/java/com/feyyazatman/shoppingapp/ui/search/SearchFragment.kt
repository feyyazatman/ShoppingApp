package com.feyyazatman.shoppingapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.databinding.FragmentSearchBinding
import com.feyyazatman.shoppingapp.ui.search.adapter.OnFilteredCategoryClickListener
import com.feyyazatman.shoppingapp.ui.search.adapter.OnFilteredProductClickListener
import com.feyyazatman.shoppingapp.ui.search.adapter.SearchCategoryAdapter
import com.feyyazatman.shoppingapp.ui.search.adapter.SearchProductAdapter
import com.feyyazatman.shoppingapp.ui.search.viewmodel.SearchViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnFilteredProductClickListener, OnFilteredCategoryClickListener {

    private val viewModel by viewModels<SearchViewmodel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchProduct()
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.searchState.collect { SearchDataState ->
                    if (SearchDataState.filteredData.isNotEmpty()) {
                        binding.rvSearch.adapter = SearchProductAdapter(this@SearchFragment).apply {
                            submitList(SearchDataState.filteredData)
                        }
                    } else {
                        SearchDataState.products?.let {
                            binding.rvSearch.adapter =
                                SearchProductAdapter(this@SearchFragment).apply {
                                    submitList(it)
                                }
                        }
                    }
                }
            }

            launch {
                viewModel.searchState.collect {
                    if (it.categories?.isNotEmpty() == true) {
                        binding.rvSearcCategory.adapter =
                            SearchCategoryAdapter(this@SearchFragment).apply {
                                submitList(it.categories)
                            }
                    }
                }
            }
        }
    }


    private fun searchProduct() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchProduct(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.length > 2) {
                        viewModel.searchProduct(newText)
                    } else {
                        viewModel.searchProduct("")
                    }
                }
                return false
            }

        })
    }


    override fun onFilteredProductClick(product: ProductItem) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(product)
        this.findNavController().navigate(action)
    }

    override fun onFilteredCategoryClick(category: String) {
        viewModel.searchProductByCategory(category)
    }


}