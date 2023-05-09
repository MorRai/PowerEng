package com.rai.powereng.ui.authorization.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentSignUpBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSignUpBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            emailCreateAccountButton.setOnClickListener {
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()
                if (checkData(email, password)) {
                    viewModel.signUpUser(email, password)
                }
            }

            lifecycleScope.launch {
                viewModel.signUpUserFlow.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserSignedUp = it.data
                            if (isUserSignedUp) {
                                viewModel.sendEmailVerification()
                                showToast(getString(R.string.account_created_email_sent_for_verification))
                            }
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            showToast(it.e.toString())
                        }
                    }
                }

            }

            lifecycleScope.launch {
                viewModel.sendEmailVerificationFlow.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isEmailVerification = it.data
                            if (isEmailVerification) {
                                val resultNav =
                                    findNavController().popBackStack(R.id.auth_nav_graph, true)
                                if (resultNav.not()) {
                                    findNavController().navigate(R.id.contentFragment)
                                }
                            }
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            showToast(it.e.toString())
                        }
                    }
                }
            }
        }
    }

    private fun checkData(
        email: String,
        password: String,
    ): Boolean {
        var checkResult = true
        if (email.isEmpty()) {
            showToast(getString(R.string.email_is_empty))
            checkResult = false
        }
        if (password.isEmpty()) {
            showToast(getString(R.string.password_is_empty))
            checkResult = false
        }
        return checkResult
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}