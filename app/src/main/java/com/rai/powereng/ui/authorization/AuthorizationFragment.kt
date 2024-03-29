package com.rai.powereng.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmetAuthorizationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {

    private var _binding: FragmetAuthorizationBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmetAuthorizationBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            emailSignInButton.setOnClickListener {
                findNavController().navigate(R.id.action_authorizationFragment_to_signInFragment)
            }
            emailCreateAccountButton.setOnClickListener {
                findNavController().navigate(R.id.action_authorizationFragment_to_signUpFragment)
            }
            continueWork.setOnClickListener {
                val resultNav = findNavController().popBackStack(R.id.auth_nav_graph, true)
                if (resultNav.not()) {
                    // we can't open new destination with this action
                    // --> we opened Auth flow from splash
                    // --> need to open main graph
                    findNavController().navigate(R.id.contentFragment)
                }
            }
            viewModel.getCurrentUserResponse()


            val currentUser = viewModel.getCurrentUserResponse().value
            if (currentUser != null && !currentUser.isEmailVerified) {
                findNavController().navigate(R.id.action_authorizationFragment_to_verifyEmailFragment)
            } else if (currentUser != null) {
                val resultNav = findNavController().popBackStack(R.id.auth_nav_graph, true)
                if (resultNav.not()) {
                    findNavController().navigate(R.id.contentFragment)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}