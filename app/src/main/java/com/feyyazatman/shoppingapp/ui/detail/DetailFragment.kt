package com.feyyazatman.shoppingapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.databinding.FragmentDetailBinding
import com.feyyazatman.shoppingapp.ui.detail.viewmodel.DetailViewmodel
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private val navigationArgs: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = navigationArgs.productItem
        binding.productItem = item

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.amount.collect{
                    binding.tvAmount.text = it.toString()
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
        }
    }


    fun navigateBack() {
        this.findNavController().navigate(R.id.action_detailFragment_to_categoryFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}