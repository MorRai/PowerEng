package com.rai.powereng.ui.tabs.usersRating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rai.powereng.adapter.UsersRatingAdapter
import com.rai.powereng.databinding.FragmentUsersRatingBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersRatingContentFragment : Fragment() {
    private var _binding: FragmentUsersRatingBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<UsersRatingContentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentUsersRatingBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val adapter =
                UsersRatingAdapter(requireContext())

            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.usersScoreFlow.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            isVisibleProgressBar(false)
                            adapter.submitList(response.data)
                        }
                        is Response.Failure -> {
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(),
                                response.e.message ?: "", Toast.LENGTH_SHORT
                            ).show()
                        }
                        Response.Loading -> {
                            isVisibleProgressBar(true)
                        }
                    }
                }
            }

        }
    }

    private fun isVisibleProgressBar(visible: Boolean) {
        binding.paginationProgressBar.isVisible = visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}