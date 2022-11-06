package com.feyyazatman.shoppingapp.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.feyyazatman.shoppingapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {


    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.authState.collect{
                    when(it) {
                        MainEvent.toAuth -> findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                        MainEvent.toCategory -> findNavController().navigate(R.id.action_mainFragment_to_categoryFragment)
                        MainEvent.toEmpty -> {}
                    }
                }
            }
        }
    }

}