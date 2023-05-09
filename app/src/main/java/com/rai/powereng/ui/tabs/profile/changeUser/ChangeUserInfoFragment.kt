package com.rai.powereng.ui.tabs.profile.changeUser

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentChangeUserInfoBinding
import com.rai.powereng.extensions.compressAndOptimizeImage
import com.rai.powereng.model.Response
import com.rai.powereng.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeUserInfoFragment : Fragment() {

    private var _binding: FragmentChangeUserInfoBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private var selectedImageUri: Uri? = null

    private val viewModel by viewModel<ChangeUserInfoVievModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentChangeUserInfoBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            val user = viewModel.getCurrentUserResponse().value
            if (user != null ){
                bindUserInfo(user)
            }

            lifecycleScope.launch {
                viewModel.signOutResponse.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserSignedOut = it.data
                            if (isUserSignedOut) {
                                findNavController().navigate(R.id.action_changeUserInfoFragment_to_profileContainerFragment)
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

            lifecycleScope.launch {
                viewModel.updateUserResponse.collect {
                    when (it) {
                        is Response.Loading -> progressBar.isVisible = true
                        is Response.Success -> {
                            progressBar.isVisible = false
                            val isUserUpdate = it.data
                            if (isUserUpdate) {
                                findNavController().popBackStack()
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

            save.setOnClickListener {
                viewModel.updateUser(emailEditText.text.toString(),nameEditText.text.toString(),selectedImageUri?.toString())
            }

            changePhotoText.setOnClickListener {
                pickImage.launch("image/*")
            }
        }

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launch {
                val compressedUri = withContext(Dispatchers.IO) {
                    uri.compressAndOptimizeImage(get())
                }
                binding.profileImage.load(compressedUri)
                selectedImageUri = compressedUri
            }
        }
    }

    private fun bindUserInfo(user: User) {
        with(binding) {
            nameEditText.setText(user.displayName)
            emailEditText.setText(user.email)
            idEditText.setText(user.uid)
            profileImage.load(user.photoUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}