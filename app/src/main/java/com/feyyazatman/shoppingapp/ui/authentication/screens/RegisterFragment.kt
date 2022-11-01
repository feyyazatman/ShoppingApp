package com.feyyazatman.shoppingapp.ui.authentication.screens

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
import com.feyyazatman.shoppingapp.databinding.FragmentRegisterBinding
import com.feyyazatman.shoppingapp.ui.authentication.viewmodel.AuthViewmodel
import com.feyyazatman.shoppingapp.ui.authentication.viewmodel.RegisterViewEvent
import com.feyyazatman.shoppingapp.ui.loadingprogress.LoadingProgressBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar = LoadingProgressBar(requireContext())

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.signUpState.collect {
                    when (it) {
                        is Resource.Success -> {
                            loadingProgressBar.hide()
                            showAlertDialog().show()
                        }
                        is Resource.Failure -> { loadingProgressBar.hide() }
                        is Resource.Loading -> {
                            loadingProgressBar.show()
                        }
                        else -> {}
                    }
                }
            }
            launch {
                viewModel.uiEvent.collect{
                    when(it) {
                        is RegisterViewEvent.ShowError -> {
                            Snackbar.make(requireView(), it.error, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        binding.apply {
            btnRegister.setOnClickListener {
                viewModel.signUp(
                    etUsername.text?.trim().toString(),
                    etEmail.text?.trim().toString(),
                    etPassword.text?.trim().toString(),
                    etconfirmPassword.text?.trim().toString()
                )
            }
        }
    }

    private fun showAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Service Request Result")
            .setMessage("Register Success" + "\n" + "\n" + "You are being redirected to main page")
            .setNeutralButton("Okey") { _, _ ->
                    findNavController().navigate(R.id.action_authFragment_to_categoryFragment)
            }.create()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}