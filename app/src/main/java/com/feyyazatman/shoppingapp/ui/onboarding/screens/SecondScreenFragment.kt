package com.feyyazatman.shoppingapp.ui.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.databinding.FragmentSecondScreenBinding
import com.feyyazatman.shoppingapp.main.MainActivity
import com.feyyazatman.shoppingapp.ui.onboarding.OnboardingActivity


class SecondScreenFragment : Fragment() {

    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.apply {
            btnNext.setOnClickListener {
                viewPager?.currentItem = 2
            }
            btnBack.setOnClickListener {
                viewPager?.currentItem = 0
            }
            tvSkip.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}