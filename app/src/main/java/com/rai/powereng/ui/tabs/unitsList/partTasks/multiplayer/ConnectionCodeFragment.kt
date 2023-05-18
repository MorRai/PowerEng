package com.rai.powereng.ui.tabs.unitsList.partTasks.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rai.powereng.databinding.FragmentConnectionCodeBinding
import com.rai.powereng.model.Response
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectionCodeFragment : Fragment() {
    private var _binding: FragmentConnectionCodeBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<ConnectionCodeViewModel>()

    private val args by navArgs<ConnectionCodeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentConnectionCodeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isSearchingGame) {
                    cancelGame()
                } else {
                    NavHostFragment.findNavController(this@ConnectionCodeFragment).navigateUp()
                }
            }
        })

        setFragmentResultListener("requestKey") { _, bundle ->
            val message = bundle.getString("message")
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        with(binding) {
            create.setOnClickListener {
                createGame()
            }
            join.setOnClickListener {
                joinGame()
            }
            cancel.setOnClickListener {
                cancelGame()
            }
            generateCode.setOnClickListener {
                viewModel.generateGameCode()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.responseGameCodeFlow.collect {
                    when (it) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            binding.gameCode.setText(it.data)
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
    }

    private fun cancelGame(){
        val gameCode = addPostfixCode(binding.gameCode.text.toString())
        viewModel.cancelGame(gameCode)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseCancelFlow.collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            viewModel.isSearchingGame = false
                            showCancelGameView()
                        }
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

    private fun createGame() {
        //gameCode = generateCode()
        val gameCode = addPostfixCode(binding.gameCode.text.toString())
        viewModel.createGame(gameCode)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseCreateFlow.collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            viewModel.isSearchingGame = true
                            startWaiting(gameCode)
                        }
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

    private fun startWaiting(gameCode:String){
        showWaitingForPlayersView()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.waitForPlayersToJoin(gameCode).collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            startTasks(gameCode)
                        }
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

    private fun joinGame() {
        val gameCode = addPostfixCode(binding.gameCode.text.toString())
        viewModel.joinGame(gameCode)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseFlow.collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            startTasks(gameCode)
                        }
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

    private fun addPostfixCode(gameCode:String): String {
        return gameCode + "u" + args.unitId + "p" +args.partId
    }

    private fun showWaitingForPlayersView() {
        with(binding) {
            gameCode.visibility = View.GONE
            textView4.visibility = View.GONE
            join.visibility = View.GONE
            create.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
        }
    }

    private fun showCancelGameView() {
        with(binding) {
            gameCode.visibility = View.VISIBLE
            textView4.visibility = View.VISIBLE
            join.visibility = View.VISIBLE
            create.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            cancel.visibility = View.GONE
        }
    }


    private fun startTasks(gameCode:String) {
        findNavController().navigate(
            ConnectionCodeFragmentDirections.actionConnectionCodeFragmentToPartTasksFragment(
                args.unitId,
                args.partId,
                true,
                gameCode
            )
        )
    }


    //override fun onDestroy() {
        //cancelGame() не кансалится эта фигня
      //  super.onDestroy()
    //}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}