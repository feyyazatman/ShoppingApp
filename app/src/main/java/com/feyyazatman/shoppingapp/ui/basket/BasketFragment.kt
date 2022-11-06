package com.feyyazatman.shoppingapp.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.databinding.BottomSheetLayoutBinding
import com.feyyazatman.shoppingapp.ui.basket.adapter.BottomSheetAdapter
import com.feyyazatman.shoppingapp.ui.basket.adapter.OnBasketItemClickListener
import com.feyyazatman.shoppingapp.ui.basket.viewmodel.BasketViewmodel
import com.feyyazatman.shoppingapp.ui.utils.Extensions.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : Fragment(), OnBasketItemClickListener {


    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BasketViewmodel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is Resource.Success -> {
                            if (it.result.isNotEmpty()) {
                                binding.rvBasket.isVisible = true
                                binding.rvBasket.adapter =
                                    BottomSheetAdapter(this@BasketFragment).apply {
                                        submitList(it.result.map { basketProductItem ->
                                            basketProductItem.copy(
                                                amount = (basketProductItem.subTotal / basketProductItem.price).toLong()
                                            )
                                        })
                                    }
                            } else {
                                binding.ivState.isVisible = true
                            }
                           
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Failure -> {
                            Toast.makeText(requireContext(), "Basket Is Empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {}
                    }
                }
            }
            launch {
                viewModel.subTotal.collect{
                    binding.tvSubtotal.text = "$" + it.format(2)
                }
            }

        }
        binding.apply {
            btnBuynow.setOnClickListener {
                showAlertDialog().show()
            }
            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }


    private fun showAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm Payment")
            .setMessage("Do you want to continue processing?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllBasketData()
                Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(200)
                    findNavController().navigate(R.id.action_basketFragment_to_categoryFragment)
                }

            }
            .setNegativeButton("No") { _, _ -> }.create()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onIncrementClick(basketProductItem: BasketProductItem) {
        viewModel.incraseAmount(basketProductItem)
    }

    override fun onDecrementClick(basketProductItem: BasketProductItem) {
        viewModel.decreaseAmount(basketProductItem)
    }




}