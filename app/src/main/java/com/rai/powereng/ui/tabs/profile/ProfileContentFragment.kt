package com.rai.powereng.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rai.powereng.databinding.FragmentProfileBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserScore
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileContentFragment: Fragment()  {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<ProfileContentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProfileBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userScoreFlow.collect { response ->
                when (response) {
                    is Response.Success -> {
                        //isVisibleProgressBar(false)
                        bind(response.data)
                    }
                    is Response.Failure -> {
                        //isVisibleProgressBar(false)
                        Toast.makeText(
                            requireContext(),
                            response.e.message ?: "", Toast.LENGTH_SHORT
                        ).show()
                    }
                    Response.Loading -> {
                        //isVisibleProgressBar(true)
                    }
                }
            }
        }
    }

    private fun bind(userScore: UserScore){
        with(binding){
            textDaysStrike.text = userScore.daysStrike.toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}