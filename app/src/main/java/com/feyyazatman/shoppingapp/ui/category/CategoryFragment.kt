package com.feyyazatman.shoppingapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.databinding.FragmentCategoryBinding
import com.feyyazatman.shoppingapp.ui.category.adapter.CategoryAdapter
import com.feyyazatman.shoppingapp.ui.category.adapter.OnProductClickListener
import com.feyyazatman.shoppingapp.ui.category.viewmodel.CategoryViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(), OnProductClickListener {


    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CategoryViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect{ uiState ->
                    uiState.products?.let {
                        binding.rvCategory.adapter = CategoryAdapter(this@CategoryFragment).apply {
                            submitList(it)
                        }
                    }
                }
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProductClick(product: ProductItem) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(product)
        this.findNavController().navigate(action)
    }

}