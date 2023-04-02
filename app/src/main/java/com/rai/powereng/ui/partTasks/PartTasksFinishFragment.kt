package com.rai.powereng.ui.partTasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rai.powereng.databinding.FragmentFinishPartBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PartTasksFinishFragment : Fragment() {
    private var _binding: FragmentFinishPartBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<PartTasksFinishViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentFinishPartBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

}