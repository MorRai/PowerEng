package com.rai.powereng.ui.authorization.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rai.powereng.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignUpBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}