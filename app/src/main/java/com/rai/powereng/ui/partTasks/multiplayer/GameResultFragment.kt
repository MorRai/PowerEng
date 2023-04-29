package com.rai.powereng.ui.partTasks.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.rai.powereng.databinding.FragmentGameResultBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.UserMultiplayer
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameResultFragment  : DialogFragment() {
    private var _binding: FragmentGameResultBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<GameResultViewModel>()

    private val args by navArgs<GameResultFragmentArgs>()


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


        binding.continueButton.setOnClickListener {

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAnswers(args.gameCode).collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        bind(it.data)
                    }
                    is Response.Failure -> {
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



    private fun bind(data: List<UserMultiplayer>) {
        with(binding) {
            val sortedData = data.sortedByDescending { it.isComplete }
            if (sortedData.size == 2) {
                sortedData[0].let {
                    user1Name.text = it.name
                    user1Photo.load(it.image)
                    if (it.isComplete ) {
                        user1Score.text = it.score.toString()
                        user1Time.text = getTimeSting(it.time)
                    }
                }
                sortedData[1].let {
                    user2Name.text = it.name
                    user2Photo.load(it.image)
                    if (it.isComplete ) {
                        waitingProgressBar.visibility = View.INVISIBLE
                        resultTextView.visibility = View.VISIBLE
                        user2Score.text = it.score.toString()
                        user2Time.text = getTimeSting(it.time)
                    }else{
                        waitingProgressBar.visibility = View.VISIBLE
                        resultTextView.visibility = View.INVISIBLE
                    }
                }
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Количество игроков не равно 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getTimeSting(elapsedSeconds: Long): String {  //нужно вынести есть функция в парт таск
        val elapsedMinutes = elapsedSeconds / 60
        val remainingSeconds = elapsedSeconds % 60
        return String.format("%d:%02d", elapsedMinutes, remainingSeconds)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}