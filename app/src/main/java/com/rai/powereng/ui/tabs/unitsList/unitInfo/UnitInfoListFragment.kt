package com.rai.powereng.ui.tabs.unitsList.unitInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rai.powereng.databinding.FragmentUnitInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnitInfoListFragment : Fragment() {

    private var _binding: FragmentUnitInfoBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<UnitInfoListViewModel>()
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

    }


    private fun isVisibleProgressBar(visible: Boolean) {
        binding.paginationProgressBar.isVisible = visible
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}