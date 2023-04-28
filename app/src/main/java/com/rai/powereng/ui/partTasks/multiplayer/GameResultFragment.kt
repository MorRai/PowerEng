package com.rai.powereng.ui.partTasks.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rai.powereng.databinding.FragmentGameResultBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameResultFragment  : DialogFragment() {
    private var _binding: FragmentGameResultBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<GameResultViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentGameResultBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}