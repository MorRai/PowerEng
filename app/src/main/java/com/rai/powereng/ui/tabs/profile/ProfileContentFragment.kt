package com.rai.powereng.ui.tabs.profile


import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import coil.load
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentProfileBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.User
import com.rai.powereng.model.UserScore
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfileContentFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<ProfileContentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentProfileBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userScoreFlow.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            progressBar.isVisible = false
                            bindScore(response.data)
                        }
                        is Response.Failure -> {
                            progressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                response.e.message ?: "", Toast.LENGTH_SHORT
                            ).show()
                            bindScore(UserScore())
                        }
                        Response.Loading -> {
                            progressBar.isVisible = true
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userAuthFlow.collect { user ->
                    if (user == null) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.user_not_authorized), Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        bindUserInfo(user)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signOutResponse.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserSignedOut = it.data
                            if (isUserSignedOut) {
                                Navigation.findNavController(requireActivity(), R.id.nav_container)
                                    .navigate(R.id.action_contentFragment_to_auth_nav_graph)
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

            signOut.setOnClickListener {
                viewModel.signOutUser()
            }

            changeInfo.setOnClickListener {
                findNavController().navigate(R.id.action_profileContentFragment_to_changeUserInfoFragment)
            }

        }
    }

    private fun bindScore(userScore: UserScore) {
        with(binding) {
            textDaysStrike.text = userScore.daysStrike.toString()
            val matrix = ColorMatrix().apply { setSaturation(0f) }
            val greyFilter = ColorMatrixColorFilter(matrix)
            layoutDaysStrike.forEach { child ->
                if (child is ImageView) {
                    val lastDigit = resources.getResourceName(child.id).last().digitToInt()
                    child.colorFilter = if (lastDigit <= userScore.daysStrike) null else greyFilter
                }
            }

            textViewScore.text = userScore.score.toString()
            textViewPart.text = userScore.part.toString()
            textViewUnit.text = userScore.unit.toString()
        }
    }

    private fun bindUserInfo(user: User) {
        with(binding) {
            nameUser.text = user.displayName
            userEmail.text = user.email
            val date = Date(user.registrationTimeMillis ?: 0)
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("eng"))
            joinedUser.text = buildString {
                append("Joined: ")
                append(dateFormat.format(date))
            }
            profileImage.load(user.photoUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}