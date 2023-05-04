package com.rai.powereng.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentSplashBinding
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
        return FragmentSplashBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            viewModel.geCurrentUserResponse()

            val navController = Navigation.findNavController(requireActivity(), R.id.nav_container)
            val mainGraph = navController.navInflater.inflate(R.navigation.nav_graph)
            val currentUser = viewModel.geCurrentUserResponse().value
            if ( currentUser !=null && currentUser.isEmailVerified) {
                mainGraph.setStartDestination(R.id.contentFragment)
            } else{
                mainGraph.setStartDestination (R.id.auth_nav_graph)
            }
            Handler(Looper.myLooper()!!).postDelayed({navController.graph = mainGraph},2000)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}