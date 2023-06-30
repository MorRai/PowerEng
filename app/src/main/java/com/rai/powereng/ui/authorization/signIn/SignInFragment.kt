package com.rai.powereng.ui.authorization.signIn

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentSignInBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSignInBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            emailSignInButton.setOnClickListener {
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()
                viewModel.signUpUser(email, password)
            }
            resendPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
            }


            lifecycleScope.launch {
                viewModel.signInResponse.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserSignedIn = it.data
                            if (isUserSignedIn) {
                                val resultNav =
                                    findNavController().popBackStack(R.id.auth_nav_graph, true)
                                if (resultNav.not()) {
                                    findNavController().navigate(R.id.contentFragment)
                                }
                            }
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                it.e.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            googleSignInButton.setOnClickListener {
                viewModel.oneTapSignIn()
            }

            lifecycleScope.launch {
                viewModel.oneTapSignInResponse
                    .collect { response ->
                        when (response) {
                            is Response.Loading -> progressBar.isVisible = true
                            is Response.Success -> {
                                progressBar.isVisible = false
                                response.data?.let {
                                    launch(it)
                                }
                            }
                            is Response.Failure -> {
                                progressBar.isVisible = false
                                Toast.makeText(
                                    requireContext(),
                                    response.e.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }

            lifecycleScope.launch {
                viewModel.signInWithGoogleResponse
                    .collect { response ->
                        when (response) {
                            is Response.Loading -> progressBar.isVisible = true
                            is Response.Success -> {
                                progressBar.isVisible = false
                                val isUserSignedIn = response.data
                                if (isUserSignedIn) {
                                    val resultNav =
                                        findNavController().popBackStack(R.id.auth_nav_graph, true)
                                    if (resultNav.not()) {
                                        findNavController().navigate(R.id.contentFragment)
                                    }
                                }
                            }
                            is Response.Failure -> {
                                progressBar.isVisible = false
                                Toast.makeText(
                                    requireContext(),
                                    response.e.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    viewModel.signInWithGoogle(googleIdToken!!)
                } catch (it: ApiException) {
                    Toast.makeText(
                        requireContext(),
                        it.status.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}