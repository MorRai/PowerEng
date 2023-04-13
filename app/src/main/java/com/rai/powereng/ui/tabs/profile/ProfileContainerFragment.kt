package com.rai.powereng.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rai.powereng.R
import com.rai.powereng.databinding.CheckAuthBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileContainerFragment : Fragment() {
    private var _binding: CheckAuthBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<ProfileContainerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return CheckAuthBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.getAuthStateResponse()
        if (viewModel.getCurrentUserResponse().value != null){
            findNavController().navigate(R.id.action_profileContainerFragment_to_profileContentFragment)
        }
        with(binding) {
            goToAuth.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.nav_container)
                    .navigate(R.id.action_contentFragment_to_auth_nav_graph)
            }
        }
    }
}