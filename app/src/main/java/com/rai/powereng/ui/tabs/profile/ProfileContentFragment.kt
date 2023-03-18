package com.rai.powereng.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentUsersRatingBinding

class ProfileContentFragment: Fragment()  {
    private var _binding: FragmentUsersRatingBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUsersRatingBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            button.setOnClickListener {
                editTextNumber.setText("6666")
            }
            button2.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.nav_container)
                    .navigate(R.id.action_contentFragment_to_auth_nav_graph)
            }
        }
    }
}