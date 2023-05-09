package com.rai.powereng.ui.tabs.unitsList.partTasks

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rai.powereng.databinding.DialogPartConfirmBinding

class PartConfirmFragment: DialogFragment() {
    private var _binding: DialogPartConfirmBinding? = null
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
        return DialogPartConfirmBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            description.text = "Do you want to go to the unit ${args.unitNum}, part ${args.part} ?"
            startTasks.setOnClickListener {
                findNavController().navigate(
                    PartConfirmFragmentDirections.actionPartConfirmFragmentToPartTasksFragment(
                        args.unitNum,
                        args.part
                    )
                )
            }
            startWithFriend.setOnClickListener {
                findNavController().navigate(
                    PartConfirmFragmentDirections.actionPartConfirmFragmentToConnectionCodeFragment(
                        args.unitNum,
                        args.part
                    )
                )
            }

        }

        val window = dialog?.window
        val layoutParams = window?.attributes
        layoutParams?.apply {
            gravity = Gravity.TOP or Gravity.START
            x = args.viewX.toInt() + args.viewWidth / 2
            y = args.viewY.toInt() + args.viewHeight /2
        }
        window?.attributes = layoutParams
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setDimAmount(0.0f)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}