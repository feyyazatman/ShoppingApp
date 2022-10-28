package com.feyyazatman.shoppingapp.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feyyazatman.shoppingapp.databinding.ActivityOnboardingBinding
import com.feyyazatman.shoppingapp.ui.onboarding.adapter.OnboardingAdapter
import com.feyyazatman.shoppingapp.ui.onboarding.screens.FirstScreenFragment
import com.feyyazatman.shoppingapp.ui.onboarding.screens.SecondScreenFragment
import com.feyyazatman.shoppingapp.ui.onboarding.screens.ThirdScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val fragmentList = arrayListOf(
            FirstScreenFragment(),
            SecondScreenFragment(),
            ThirdScreenFragment()
        )

        val dotsIndicator = binding.dotsIndicator
        val adapter = OnboardingAdapter(fragmentList,this.supportFragmentManager,lifecycle)
        binding.viewPager.adapter = adapter
        dotsIndicator.attachTo(binding.viewPager)

    }

}