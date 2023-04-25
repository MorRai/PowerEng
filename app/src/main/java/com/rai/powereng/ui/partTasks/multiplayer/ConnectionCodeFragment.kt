package com.rai.powereng.ui.partTasks.multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        with(binding) {
            create.setOnClickListener {
                createGame()
            }
            join.setOnClickListener {
                joinGame()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}