package com.rai.powereng.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSplashBinding.inflate(inflater, container, false).also { binding ->
            _binding = binding
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_container)
        val mainGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        lifecycleScope.launchWhenStarted {
            try {
                withTimeout(2000) {
                    viewModel.geCurrentUserResponse().collect { currentUser ->
                        if (currentUser != null && currentUser.isEmailVerified) {
                            mainGraph.setStartDestination(R.id.contentFragment)
                        } else {
                            mainGraph.setStartDestination(R.id.auth_nav_graph)
                        }
                        if (currentUser != null) {
                            viewModel.updateUserInfo(currentUser.uid)
                            delay(300)
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                val currentUser = viewModel.geCurrentUserResponse().value
                if (currentUser != null && currentUser.isEmailVerified) {
                    mainGraph.setStartDestination(R.id.contentFragment)
                } else {
                    mainGraph.setStartDestination(R.id.auth_nav_graph)
                }
            }
            navController.graph = mainGraph
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}