package com.rai.powereng.ui.tabs.unitsList.unitInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rai.powereng.adapter.InfoUnitAdapter
import com.rai.powereng.databinding.FragmentUnitInfoBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UnitInfoListFragment : Fragment() {

    private var _binding: FragmentUnitInfoBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args by navArgs<UnitInfoListFragmentArgs>()

    private val viewModel by viewModel<UnitInfoListViewModel> {
        parametersOf(args.unitId)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentUnitInfoBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setupWithNavController(findNavController())

            val adapter = InfoUnitAdapter(requireContext())
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager

            viewLifecycleOwner.lifecycleScope.launch {
               viewModel.unitsFlow.collect { result->
                    when (result) {
                        is Response.Success -> {
                            isVisibleProgressBar(false)
                            adapter.submitList(result.data)
                        }
                        is Response.Failure -> {
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(),
                                result.e.message ?: "", Toast.LENGTH_SHORT
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