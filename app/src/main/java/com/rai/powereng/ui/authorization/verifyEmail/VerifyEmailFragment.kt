package com.rai.powereng.ui.authorization.verifyEmail

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
import com.rai.powereng.databinding.FragmentVerifyEmailBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyEmailFragment : Fragment() {
    private var _binding: FragmentVerifyEmailBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<VerifyEmailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentVerifyEmailBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            continueAction.setOnClickListener {
                val resultNav = findNavController().popBackStack(R.id.auth_nav_graph, true)
                if (resultNav.not()) {
                    findNavController().navigate(R.id.contentFragment)
                }
            }
            relogin.setOnClickListener {
                viewModel.signOutUser()
            }

            lifecycleScope.launch {
                viewModel.signOutResponse.collect {response ->
                    when (response) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserSignedOut = response.data
                            if (isUserSignedOut) {
                                findNavController().popBackStack()
                            }
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(requireContext(),
                                response.e.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}