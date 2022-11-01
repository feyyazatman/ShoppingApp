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
import com.feyyazatman.shoppingapp.databinding.FragmentLoginBinding
import com.feyyazatman.shoppingapp.ui.authentication.viewmodel.AuthViewmodel
import com.feyyazatman.shoppingapp.ui.loadingprogress.LoadingProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar = LoadingProgressBar(requireContext())

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.loginState.collect {
                    when (it) {
                        is Resource.Success -> {
                            loadingProgressBar.hide()
                            showAlertDialog(
                                "Login Success",
                                "You are being redirected to main page"
                            ).show()

                        }
                        is Resource.Failure -> {
                            loadingProgressBar.hide()
                            showAlertDialog("Login Failed", it.exception.toString()).show()
                        }
                        is Resource.Loading -> {
                            loadingProgressBar.show()
                        }
                        else -> {}
                    }
                }
            }
            launch {
                if (viewModel.currentUser != null) {
                    findNavController().navigate(R.id.action_authFragment_to_categoryFragment)
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.signIn(
                binding.etEmail.text?.trim().toString(),
                binding.etPassword.text?.trim().toString()
            )
        }
    }

    private fun showAlertDialog(message: String, statement: String?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Service Request Result")
            .setMessage(message + "\n" + "\n" + statement)
            .setNeutralButton("Okey") { _, _ ->
                findNavController().navigate(R.id.action_authFragment_to_categoryFragment)
            }.create()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}