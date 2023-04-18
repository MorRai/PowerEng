package com.rai.powereng.ui.partTasks

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.descendants
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentPartTasksBinding
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class PartTasksFragment : Fragment() {
    private var _binding: FragmentPartTasksBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }


    private var tts: TextToSpeech? = null

    private val viewModel by viewModel<PartTasksViewModel>()

    private val args by navArgs<PartTasksFragmentArgs>()

    private var taskNum = 1
    private var amountTasks = 0
    private var amountMistakes= 0
    private var workWithList = false
    private val listErrors = mutableListOf<Int>()

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
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.dialogResultId.dialogResult)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        val bottomSheetCallback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        viewModel.getTasksUser(args.unitId, args.partId, taskNum)
        viewModel.tasksFlow.onEach {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    bind(it.data)
                }
                is Response.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        it.e.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getTasksAmount(args.unitId, args.partId)
        viewModel.tasksAmountFlow.onEach {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    amountTasks = it.data
                }
                is Response.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        it.e.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.progress.onEach {
            binding.progressPath.max = amountTasks
            binding.progressPath.progress = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun clearForm() {
        with(binding) {
            if (workWithList && listErrors.size > 0 ) {
                viewModel.getTasksUser(args.unitId, args.partId, listErrors[0])
            } else if (taskNum <= amountTasks) {
                viewModel.getTasksUser(args.unitId, args.partId, taskNum)
            } else if (listErrors.size > 0) {
                itemTranclate.answerBox.removeAllViews()
                itemTranclate.optionBox.removeAllViews()
                showErrorsScreen()
            } else {
                findNavController().navigate(PartTasksFragmentDirections.actionPartTasksFragmentToPartTasksFinishFragment(args.unitId,args.partId,amountMistakes))
            }
        }
    }


    private fun showErrorsScreen() {
        with(binding) {
            val transitionEND = TransitionSet().apply {
                addTransition(Fade(Fade.IN)).addTransition(Slide(Gravity.END))
                duration = 500
            }
            TransitionManager.beginDelayedTransition(contentLayout, transitionEND)
            errorsScreen.root.visibility = View.VISIBLE
            description.visibility = View.GONE
            itemTranclate.root.visibility = View.GONE
            itemMissingWord.root.visibility = View.GONE
            exerciseInfo.visibility = View.GONE
            itemListen.root.visibility = View.GONE
            check.setOnClickListener {
                workWithList = true
                amountMistakes = listErrors.size
                viewModel.getTasksUser(args.unitId, args.partId, listErrors[0])
            }
        }
    }


    private fun bind(task: TaskData) {
        with(binding) {
            val transitionEND = TransitionSet().apply {
                addTransition(Fade(Fade.IN)).addTransition(Slide(Gravity.END))
                duration = 500
            }
            TransitionManager.beginDelayedTransition(contentLayout, transitionEND)

            exerciseInfo.visibility = View.GONE
            itemMissingWord.root.visibility = View.GONE
            itemTranclate.root.visibility = View.GONE
            itemListen.root.visibility = View.GONE
            errorsScreen.root.visibility = View.GONE
            itemTranclate.answerBox.removeAllViews()
            itemTranclate.optionBox.removeAllViews()
            when (task.typeTask) {
                1, 2 -> {
                    description.text = "Translate the sentence:"
                    itemTranclate.root.visibility = View.VISIBLE
                    exerciseInfo.visibility = View.VISIBLE
                    bindForTranslate(task)
                }
                3 -> {
                    description.text = "Insert a word"
                    exerciseInfo.visibility = View.VISIBLE
                    itemMissingWord.root.visibility = View.VISIBLE
                    bindForMissingWord(task)
                }
                4 -> {
                    description.text = "Enter what you heard"
                    itemTranclate.root.visibility = View.VISIBLE
                    itemListen.root.visibility = View.VISIBLE
                    bindForListen(task)
                }
            }

            dialogResultId.buttonContinue.setOnClickListener {
                if (taskNum <= amountTasks) {
                    taskNum += 1
                }
                accessibilityButtons(contentLayout, true)

                bottomSheet.visibility = View.GONE
                clearForm()
            }
        }
    }


    private fun bindForListen(task: TaskData) {
        with(binding) {
            val textForListen = task.answer
            itemListen.playSound.setOnClickListener {
                speak(textForListen)
            }
            itemListen.playSoundSlow.setOnClickListener {
                speak(textForListen, 0.5f)
            }

            check.setOnClickListener {
                var checkAnswer = ""
                for (childView in itemTranclate.answerBox.children) {
                    if (childView is Button) {
                        checkAnswer = checkAnswer + " " + childView.text
                    }
                }
                checkAnswer = checkAnswer.drop(1)
                setSettingsDialog(checkAnswer == textForListen, checkAnswer, task.taskNum)
                bottomSheet.visibility = View.VISIBLE
                accessibilityButtons(contentLayout, false)
            }
            val listWords = task.answer.split(" ") + task.variants.split(" ")
            listWords.forEach {
                addTextViewToOption(it, task.typeTask)
            }

        }

    }


    private fun bindForMissingWord(task: TaskData) {
        with(binding) {
            val checkAnswer = task.answer
            var answerString = ""
            check.setOnClickListener {
                when (itemMissingWord.wordVariants.checkedRadioButtonId) {
                    R.id.variantOne -> answerString = itemMissingWord.variantOne.text.toString()
                    R.id.variantTwo -> answerString = itemMissingWord.variantTwo.text.toString()
                }
                if (answerString.isBlank()) {
                    Toast.makeText(requireContext(), "You must choose an answer!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    setSettingsDialog(checkAnswer == answerString, checkAnswer, task.taskNum)
                    bottomSheet.visibility = View.VISIBLE
                    accessibilityButtons(contentLayout, false)
                }
            }
            val listWords = task.variants.split(" ")//их всегда 2
            itemMissingWord.variantOne.text = listWords[0]
            itemMissingWord.variantTwo.text = listWords[1]
            exerciseInfo.text = task.question
        }
    }

    private fun bindForTranslate(task: TaskData) {
        with(binding) {
            val checkAnswer = task.answer
            exerciseInfo.text = task.question
            check.setOnClickListener {
                var answerString = ""
                for (childView in itemTranclate.answerBox.children) {
                    if (childView is Button) {
                        answerString = answerString + " " + childView.text
                    }
                }
                answerString = answerString.drop(1)
                setSettingsDialog(checkAnswer == answerString, checkAnswer, task.taskNum)
                bottomSheet.visibility = View.VISIBLE
                accessibilityButtons(contentLayout, false)
            }
            val listWords = task.answer.split(" ") + task.variants.split(" ")
            listWords.forEach {
                addTextViewToOption(it, task.typeTask)
            }
        }
    }

    private fun addTextViewToOption(text: String, typeTask: Int) {
        val textView = Button(requireContext())
        textView.text = text
        textView.setPadding(16, 16, 16, 16)
        //textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            if (typeTask == 1 || typeTask == 4) {
                speak(text)
            }
            addTextViewToAnswer(text, typeTask)
            binding.itemTranclate.optionBox.removeView(textView)
        }
        binding.itemTranclate.optionBox.addView(textView)
    }

    private fun addTextViewToAnswer(text: String, typeTask: Int) {
        val textView = Button(requireContext())
        textView.text = text
        textView.setPadding(16, 16, 16, 16)
        //textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            addTextViewToOption(text, typeTask)
            binding.itemTranclate.answerBox.removeView(textView)
        }
        binding.itemTranclate.answerBox.addView(textView)
    }

    private fun setSettingsDialog(answerIsTrue: Boolean, answer: String, numTask: Int) {
        with(binding.dialogResultId) {
            if (answerIsTrue) {
                if (workWithList) {
                    listErrors.removeAt(0)
                }
                viewModel.addProgress()
                dialogResult.setBackgroundColor(resources.getColor(R.color.green_lite, null))
                resultAnswer.setTextColor(resources.getColor(R.color.green, null))
                textAnswer.setTextColor(resources.getColor(R.color.green, null))
                buttonContinue.setBackgroundColor(resources.getColor(R.color.green, null))
                buttonContinue.setTextColor(resources.getColor(R.color.white, null))
                resultAnswer.text = "Success!"
            } else {
                if (!workWithList) {
                    listErrors.add(numTask)
                }
                dialogResult.setBackgroundColor(resources.getColor(R.color.red_lite, null))
                buttonContinue.setBackgroundColor(resources.getColor(R.color.red, null))
                buttonContinue.setTextColor(resources.getColor(R.color.white, null))
                resultAnswer.setTextColor(resources.getColor(R.color.red, null))
                textAnswer.setTextColor(resources.getColor(R.color.red, null))
                trueAnswerText.setTextColor(resources.getColor(R.color.red, null))
                resultAnswer.text = "Error!"
                trueAnswerText.text = "Correct answer: "
            }
            textAnswer.text = answer
        }
    }


    private fun accessibilityButtons(layout: ViewGroup, accessibility: Boolean) {
        layout.descendants.filterIsInstance<Button>().forEach { button ->
            button.isClickable = accessibility
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    private fun speak(text: String, speed: Float = 1.0f) {
        tts = TextToSpeech(requireContext()) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts!!.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language not supported!")

                }
                tts!!.setSpeechRate(speed)
                tts!!.speak(text, TextToSpeech.QUEUE_ADD, null)

            } else {
                Log.e("TTS", "Initialization failed!")
            }
        }
    }

}