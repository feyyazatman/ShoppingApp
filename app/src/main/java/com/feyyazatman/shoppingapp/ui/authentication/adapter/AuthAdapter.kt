package com.feyyazatman.shoppingapp.ui.authentication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.feyyazatman.shoppingapp.ui.authentication.screens.LoginFragment
import com.feyyazatman.shoppingapp.ui.authentication.screens.RegisterFragment


class AuthAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> { LoginFragment() }
            else -> { RegisterFragment() }

        }
    }

}