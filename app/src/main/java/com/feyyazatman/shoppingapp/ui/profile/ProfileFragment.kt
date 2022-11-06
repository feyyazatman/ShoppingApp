package com.feyyazatman.shoppingapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.databinding.FragmentProfileBinding
import com.feyyazatman.shoppingapp.ui.authentication.viewmodel.AuthViewmodel
import com.feyyazatman.shoppingapp.ui.profile.viewmodel.ProfileViewmodel
import com.feyyazatman.shoppingapp.ui.utils.Extensions.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {


    private val viewModelAuth by viewModels<AuthViewmodel>()
    private val viewModelProfile by viewModels<ProfileViewmodel>()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onStart() {
        super.onStart()
        viewModelProfile.getSubTotalPrice() // not working onCreate() method when navigate back basketFragment to ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            launch {
                viewModelProfile.uiState.collect{
                    when(it) {
                        is Resource.Success -> {
                            binding.apply {
                                tvUsernameVal.text = it.result.username
                                tvMailVal.text = it.result.email
                            }
                        }
                        is Resource.Failure -> {}
                        is Resource.Loading -> {}
                        else -> {}
                    }
                }
            }

            launch {
                viewModelProfile.subTotal.collect {
                    if (it != null) {
                        binding.tvTotalAmount.text = "$" + it.format(2)
                    }
                }
            }
        }

        binding.apply {
            btnLogout.setOnClickListener {
                showAlertDialog().show()
            }
            btnMarket.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_basketFragment)
            }
        }
    }


    private fun showAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to exit")
            .setNegativeButton("Yes") { _,_ ->
                viewModelAuth.logout()
                findNavController().navigate(R.id.action_profileFragment_to_authFragment)
            }
            .setPositiveButton("No") {_,_ -> }.create()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}