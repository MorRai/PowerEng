package com.rai.powereng.ui.partTasks.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var gameCode: String
    private lateinit var playerName: String

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

        playerName = FirebaseAuth.getInstance().currentUser?.displayName ?: "0"

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isSearchingGame) {
                    cancelGame()
                } else {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        setFragmentResultListener("requestKey") { requestKey, bundle ->
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
        }
    }



    private fun cancelGame(){
        gameCode = binding.gameCode.text.toString()
        viewModel.cancelGame(gameCode)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseCancelFlow.collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            viewModel.isSearchingGame = true
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
        gameCode = binding.gameCode.text.toString()
        viewModel.createGame(gameCode, playerName)
        showWaitingForPlayersView()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.waitForPlayersToJoin(gameCode).collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            startTasks()
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
        gameCode = binding.gameCode.text.toString()
        viewModel.joinGame(gameCode, playerName)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseFlow.collect {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data) {
                            startTasks()
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

    private fun generateCode(): String {
        // добавим что бы код получили с скрытым добавление урока и части
        // TODO: generate unique game code
        return "12345"
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


    private fun startTasks() {
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