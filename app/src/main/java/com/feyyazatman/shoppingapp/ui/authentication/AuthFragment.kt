package com.feyyazatman.shoppingapp.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feyyazatman.shoppingapp.databinding.FragmentAuthBinding
import com.feyyazatman.shoppingapp.ui.authentication.adapter.AuthAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
        val adapter = AuthAdapter(parentFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Sign In"
                }
                1 -> {
                    tab.text = "Register"
                }
            }
        }.attach()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}