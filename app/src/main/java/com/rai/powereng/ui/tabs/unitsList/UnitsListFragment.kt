package com.rai.powereng.ui.tabs.unitsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rai.powereng.R
import com.rai.powereng.adapter.UnitsAdapter
import com.rai.powereng.databinding.FragmentUnitsListBinding
import com.rai.powereng.model.Response
import com.rai.powereng.ui.ContentFragmentDirections
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnitsListFragment : Fragment(), PartClickListener {

    private var _binding: FragmentUnitsListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<UnitsListViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentUnitsListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            buttonReturnToTop.setOnClickListener {
                recyclerView.smoothScrollToPosition(0)
            }

            val adapter =
                UnitsAdapter(requireContext(),this@UnitsListFragment ) {
                    findNavController().navigate(UnitsListFragmentDirections.actionUnitsListFragmentToUnitInfoListFragment(
                        it.unitId))
                }

            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager

            viewLifecycleOwner.lifecycleScope.launch {
                combine(viewModel.unitsFlow, viewModel.userScoreFlow) { units, userScore ->
                    Pair(units, userScore)
                }.collect { (unitsResponse, userScoreResponse) ->
                    when (unitsResponse) {
                        is Response.Success -> {
                            isVisibleProgressBar(false)
                            adapter.submitList(unitsResponse.data)
                        }
                        is Response.Failure -> {
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(),
                                unitsResponse.e.message ?: "", Toast.LENGTH_SHORT
                            ).show()
                        }
                        Response.Loading -> {
                            isVisibleProgressBar(true)
                        }
                    }
                    when (userScoreResponse) {
                        is Response.Success -> {
                            adapter.setScore(userScoreResponse.data)
                        }
                        is Response.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                userScoreResponse.e.message ?: "", Toast.LENGTH_SHORT
                            ).show()
                        }
                        Response.Loading -> {
                            // do nothing
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
    override fun onPartClickListener(unitNum: Int,part: Int,position: Int, view: View) {
        Navigation.findNavController(requireActivity(), R.id.nav_container)
            .navigate(ContentFragmentDirections.actionContentFragmentToTasksNavGraph(unitNum,part,position, view.x, view.y, view.width, view.height))

    }
}