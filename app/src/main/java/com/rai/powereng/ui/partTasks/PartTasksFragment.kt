package com.rai.powereng.ui.partTasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentPartTasksBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PartTasksFragment: Fragment() {
    private var _binding: FragmentPartTasksBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<PartTasksViewModel>()
    private val args by navArgs<PartTasksFragmentArgs>()

    private var taskNum = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPartTasksBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTasksUser(args.unitId,args.partId,taskNum)

        viewModel.tasksFlow.onEach {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    //progressBar.isVisible = false
                    bind(it.data)
                }
                is Response.Failure -> {
                    //progressBar.isVisible = false
                    Toast.makeText(requireContext(),
                        it.e.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }


    private fun clearForm(){
        with(binding) {
            itemTranclate.answerBox.removeAllViews()
            itemTranclate.optionBox.removeAllViews()
        }
        viewModel.getTasksUser(args.unitId,args.partId,taskNum)
    }

    fun bind(task:TaskData){
        with(binding) {
            val checkAnswer = task.sentenceEN
            description.text = "Переведите предложение:"
            check.setOnClickListener {
                var answerString = ""
                for (childView in itemTranclate.answerBox.children) {
                    if (childView is Button)  {
                        answerString = answerString + " " + childView.text
                    }
                }
                answerString = answerString.drop(1)
                if (checkAnswer == answerString) {
                    Toast.makeText(requireContext(), "успех", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "провал", Toast.LENGTH_SHORT).show()
                }
                taskNum += 1
                clearForm()

            }
            val listWords = task.composeKitEN.split(" ") + task.sentenceEN.split(" ")
            listWords.forEach {
                addTextViewToOption(it)
            }
            itemTranclate.exerciseInfo.text = task.sentenceRUS
        }
    }

    fun addTextViewToOption(text:String){
        val textView =Button(requireContext())
        textView.text = text
        textView.setPadding(16,16,16,16)
        textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            addTextViewToAnswer(text)
            binding.itemTranclate.optionBox.removeView(textView)
        }
        binding.itemTranclate.optionBox.addView(textView)
    }

    fun addTextViewToAnswer(text:String){
        val textView =Button(requireContext())
        textView.text = text
        textView.setPadding(16,16,16,16)
        textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            addTextViewToOption(text)
            binding.itemTranclate.answerBox.removeView(textView)
        }
        binding.itemTranclate.answerBox.addView(textView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}