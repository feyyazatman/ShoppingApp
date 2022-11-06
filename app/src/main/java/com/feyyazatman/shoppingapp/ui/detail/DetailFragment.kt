package com.feyyazatman.shoppingapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.data.model.ProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.databinding.FragmentDetailBinding
import com.feyyazatman.shoppingapp.ui.detail.viewmodel.DetailViewmodel
import com.feyyazatman.shoppingapp.ui.utils.Extensions.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val navigationArgs: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewmodel>()


    override fun onStart() {
        super.onStart()
        viewModel.getSubTotalPrice()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = navigationArgs.productItem
        binding.productItem = item

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.amount.collect {
                    binding.tvAmount.text = it.toString()
                }
            }
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.i("result", it.result.toString())
                        }
                        is Resource.Failure -> {}
                        is Resource.Loading -> {}
                        else -> {}
                    }
                }
            }

            launch {
                viewModel.subTotal.collect {
                    if (it != null) {
                        binding.tvTotalAmount.text = "$" + it.format(2)
                    }
                }
            }

        }

        binding.apply {
            ivAdd.setOnClickListener {
                viewModel.incraseAmount()
            }
            ivRemove.setOnClickListener {
                viewModel.decreaseAmount()
            }
            btnBack.setOnClickListener {
                navigateBack()
            }
            btnAdd.setOnClickListener {
                addToCart(item)
            }
            btnMarket.setOnClickListener {
                findNavController().navigate(R.id.action_detailFragment_to_basketFragment)
            }
        }
    }



    private fun navigateBack() {
        val action = findNavController().previousBackStackEntry?.destination?.id
        action?.let { this.findNavController().navigate(it) }


    }

    private fun addToCart(productItem: ProductItem) {
        viewModel.addToCart(productItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}