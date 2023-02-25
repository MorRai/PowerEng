package com.rai.powereng.ui.wordsUnitsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rai.powereng.databinding.FragmentWordsUnitsListBinding

class WordsUnitsListFragment : Fragment() {
    private var _binding: FragmentWordsUnitsListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWordsUnitsListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}