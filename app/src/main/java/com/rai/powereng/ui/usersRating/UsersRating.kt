package com.rai.powereng.ui.usersRating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rai.powereng.databinding.FragmentUsersRatingBinding


class UsersRating : Fragment() {
    private var _binding: FragmentUsersRatingBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUsersRatingBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }
}