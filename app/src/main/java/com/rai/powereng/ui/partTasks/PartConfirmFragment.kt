package com.rai.powereng.ui.partTasks

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rai.powereng.databinding.FragmentPartConfirmBinding

class PartConfirmFragment: Fragment() {
    private var _binding: FragmentPartConfirmBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args by navArgs<PartConfirmFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPartConfirmBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            description.text = "Хотите перейти к разделу ${args.unitNum}, главе ${args.part} ?"
            cancel.setOnClickListener {
                findNavController().popBackStack()
            }
            startTasks.setOnClickListener {

                findNavController().navigate(PartConfirmFragmentDirections.actionPartConfirmFragmentToPartTasksFragment(args.unitNum,args.part))
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}