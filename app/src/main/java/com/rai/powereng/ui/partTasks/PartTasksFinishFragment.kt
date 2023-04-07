package com.rai.powereng.ui.partTasks

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
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentFinishPartBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserProgressInfo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PartTasksFinishFragment : Fragment() {
    private var _binding: FragmentFinishPartBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<PartTasksFinishViewModel>()

    private val args by navArgs<PartTasksFinishFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentFinishPartBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonContinue.setOnClickListener {
                var points = 50 - 5 * args.mistakes
                if (points < 20) {
                    points = 20
                }
                val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val dateText = dateFormat.format(Date())
                viewModel.addUserInfo(
                    points = points,
                    dateText = dateText,
                    mistakes = args.mistakes,
                    unitId = args.unitId,
                    partId = args.partId
                )
            }

            lifecycleScope.launch {
                viewModel.addUserInfoFlow.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            if (it.data) {
                                val resultNav =
                                    findNavController().popBackStack(R.id.tasks_nav_graph, true)
                                if (resultNav.not()) {
                                    findNavController().navigate(R.id.contentFragment)
                                }
                            }
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                it.e.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}