package com.rai.powereng.ui.tabs.unitsList.partTasks

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.descendants
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentPartTasksBinding
import com.rai.powereng.extensions.ConfirmationDialogUtils
import com.rai.powereng.extensions.getAllTextViews
import com.rai.powereng.extensions.getTimeSting
import com.rai.powereng.model.Response
import com.rai.powereng.model.TaskData
import com.rai.powereng.model.UserMultiplayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    private var amountMistakes = 0
    private var workWithList = false
    private val listErrors = mutableListOf<Int>()
    private var correctAnswersCount = 0
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            taskNum = it.getInt("taskNum")
            amountTasks = it.getInt("amountTasks")
            amountMistakes = it.getInt("amountMistakes")
            workWithList = it.getBoolean("workWithList")
            listErrors.addAll(it.getIntegerArrayList("listErrors") ?: emptyList())
            correctAnswersCount = it.getInt("correctAnswersCount")
            startTime = it.getLong("startTime")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPartTasksBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("taskNum", taskNum)
        outState.putInt("amountTasks", amountTasks)
        outState.putInt("amountMistakes", amountMistakes)
        outState.putBoolean("workWithList", workWithList)
        outState.putIntegerArrayList("listErrors", ArrayList(listErrors))
        outState.putInt("correctAnswersCount", correctAnswersCount)
        outState.putLong("startTime", startTime)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }
        with(binding) {

            viewLifecycleOwner.lifecycleScope.launch {
                while (true) {
                    val time = (System.currentTimeMillis() - startTime) / 1000
                    textTimer.text = getTimeSting(time)
                    delay(1000) // Wait 1 second
                }
            }

            toolbar.setupWithNavController(findNavController())
            toolbar.setNavigationOnClickListener {
                showConfirmationDialog()
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showConfirmationDialog()
                }
            })

            if (args.isMultiplayer) {
                progressPath.visibility = View.GONE
                multiplayerInfo.root.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getAnswers(args.gameCode).collect {
                        when (it) {
                            is Response.Loading -> {}
                            is Response.Success -> {
                                bindMultiplayer(it.data)
                            }
                            is Response.Failure -> {
                                showToast(it.e.toString())
                            }
                        }
                    }
                }
            }


            val bottomSheetBehavior = BottomSheetBehavior.from(dialogResultId.dialogResult)
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
                        showToast(it.e.toString())
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.getTasksAmount(args.unitId, args.partId)
            viewModel.tasksAmountFlow.onEach {
                when (it) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (it.data == 0) {
                            val bundle = bundleOf("message" to getString(R.string.dont_found_tasks))
                            setFragmentResult("requestKey", bundle)
                            findNavController().popBackStack()
                        } else amountTasks = it.data
                    }
                    is Response.Failure -> { //this should not be, but just in case
                        val bundle = bundleOf("message" to getString(R.string.dont_found_tasks))
                        setFragmentResult("requestKey", bundle)
                        findNavController().popBackStack()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.progress.onEach {
                progressPath.max = amountTasks
                progressPath.progress = it
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun bindMultiplayer(data: List<UserMultiplayer>) {
        with(binding.multiplayerInfo) {
            if (data.size == 2) {
                data[0].let {
                    user1Name.text = it.name
                    user1Score.text = buildString {
                        append(getString(R.string.score_two_point))
                        append(it.score)
                    }
                }
                data[1].let {
                    user2Name.text = it.name
                    user2Score.text = buildString {
                        append(getString(R.string.score_two_point))
                        append(it.score)
                    }
                }
            } else {
                val bundle = bundleOf("message" to getString(R.string.the_game_is_destroyed))
                setFragmentResult("requestKey", bundle)
                findNavController().popBackStack()
            }
        }
    }

    private fun getNextTask() {
        when {
            workWithList && listErrors.isNotEmpty() -> {
                viewModel.getTasksUser(args.unitId, args.partId, listErrors[0])
            }
            taskNum <= amountTasks -> {
                if (args.isMultiplayer) {
                    viewModel.saveAnswers(args.gameCode, correctAnswersCount, startTime, false)
                }
                viewModel.getTasksUser(args.unitId, args.partId, taskNum)
            }
            args.isMultiplayer -> {
                viewModel.saveAnswers(args.gameCode, correctAnswersCount, startTime, true)
                findNavController().navigate(
                    PartTasksFragmentDirections.actionPartTasksFragmentToGameResultFragment(args.gameCode)
                )
            }
            listErrors.isNotEmpty() -> {
                binding.itemTranclate.answerBox.removeAllViews()
                binding.itemTranclate.optionBox.removeAllViews()
                showErrorsScreen()
            }
            else -> {
                findNavController().navigate(
                    PartTasksFragmentDirections.actionPartTasksFragmentToPartTasksFinishFragment(
                        args.unitId,
                        args.partId,
                        amountMistakes,
                        (System.currentTimeMillis() - startTime) / 1000,
                        amountTasks
                    )
                )
            }
        }
    }

    private fun showErrorsScreen() {
        with(binding) {
            val transitionSet = TransitionSet()
            setupTransition(transitionSet)
            TransitionManager.beginDelayedTransition(contentLayout, transitionSet)
            hideAllViews()
            errorsScreen.root.visibility = View.VISIBLE
            check.setOnClickListener {
                workWithList = true
                amountMistakes = listErrors.size
                getNextTask()
            }
        }
    }

    private fun bind(task: TaskData) {
        with(binding) {
            val transitionSet = TransitionSet()
            setupTransition(transitionSet)
            TransitionManager.beginDelayedTransition(contentLayout, transitionSet)
            hideAllViews()
            when (task.typeTask) {
                1, 2 -> {
                    bindForTranslate(task)
                }
                3 -> {
                    bindForMissingWord(task)
                }
                4 -> {
                    bindForListen(task)
                }
                5 -> {
                    bindForWordTranslate(task)
                }
            }
            dialogResultId.buttonContinue.setOnClickListener {
                if (taskNum <= amountTasks) {
                    taskNum += 1
                }
                accessibilityButtons(contentLayout, true)
                bottomSheet.visibility = View.GONE
                getNextTask()
            }
        }
    }

    private fun hideAllViews() {
        with(binding) {
            exerciseInfo.visibility = View.GONE
            itemMissingWord.root.visibility = View.GONE
            itemTranclate.root.visibility = View.GONE
            itemListen.root.visibility = View.GONE
            itemTaskWordTranslate.root.visibility = View.GONE
            errorsScreen.root.visibility = View.GONE
            itemTranclate.answerBox.removeAllViews()
            itemTranclate.optionBox.removeAllViews()
        }
    }

    private fun setupTransition(transitionSet: TransitionSet) {
        transitionSet.apply {
            addTransition(Fade(Fade.IN)).addTransition(Slide(Gravity.END))
            duration = 500
        }
    }


    private fun bindForWordTranslate(task: TaskData) {
        with(binding) {
            description.text = getString(R.string.translate_word)
            exerciseInfo.visibility = View.VISIBLE
            itemTaskWordTranslate.root.visibility = View.VISIBLE

            val checkAnswer = task.answer
            var answerString = ""
            check.setOnClickListener {
                when (itemTaskWordTranslate.wordVariants.checkedRadioButtonId) {
                    R.id.variantOne -> answerString =
                        itemTaskWordTranslate.variantOne.text.toString()
                    R.id.variantTwo -> answerString =
                        itemTaskWordTranslate.variantTwo.text.toString()
                    R.id.variantThree -> answerString =
                        itemTaskWordTranslate.variantThree.text.toString()
                    R.id.variantFour -> answerString =
                        itemTaskWordTranslate.variantFour.text.toString()
                }
                if (answerString.isBlank()) {
                    showToast(getString(R.string.you_must_choose_an_answer))
                } else {
                    setSettingsDialog(checkAnswer == answerString, checkAnswer, task.taskNum)
                    bottomSheet.visibility = View.VISIBLE
                    accessibilityButtons(contentLayout, false)
                }
            }
            itemTaskWordTranslate.variantOne.text = task.variants.split(" ")[0]
            itemTaskWordTranslate.variantTwo.text = task.variants.split(" ")[1]
            itemTaskWordTranslate.variantThree.text = task.variants.split(" ")[2]
            itemTaskWordTranslate.variantFour.text = task.variants.split(" ")[3]
            exerciseInfo.text = task.question
        }
    }


    private fun bindForListen(task: TaskData) {
        with(binding) {
            description.text = getString(R.string.enter_what_you_heard)
            itemListen.root.visibility = View.VISIBLE
            itemTranclate.root.visibility = View.VISIBLE


            val textForListen = task.answer
            itemListen.playSound.setOnClickListener {
                speak(textForListen)
            }
            itemListen.playSoundSlow.setOnClickListener {
                speak(textForListen, 0.5f)
            }
            check.setOnClickListener {
                val checkAnswer = itemTranclate.answerBox.children
                    .filterIsInstance<CardView>()
                    .flatMap { cardView -> cardView.getAllTextViews() }
                    .joinToString(" ") { textView -> textView.text }

                setSettingsDialog(checkAnswer == textForListen, checkAnswer, task.taskNum)
                bottomSheet.visibility = View.VISIBLE
                accessibilityButtons(contentLayout, false)
            }
            task.answer.split(" ").plus(task.variants.split(" "))
                .forEach { addTextViewToOptionAndAnswer(it, task.typeTask) }
        }

    }

    private fun bindForMissingWord(task: TaskData) {
        with(binding) {
            description.text = getString(R.string.insert_a_word)
            exerciseInfo.visibility = View.VISIBLE
            itemMissingWord.root.visibility = View.VISIBLE

            val checkAnswer = task.answer
            var answerString = ""
            check.setOnClickListener {
                when (itemMissingWord.wordVariants.checkedRadioButtonId) {
                    R.id.variantOne -> answerString = itemMissingWord.variantOne.text.toString()
                    R.id.variantTwo -> answerString = itemMissingWord.variantTwo.text.toString()
                }
                if (answerString.isBlank()) {
                    showToast(getString(R.string.you_must_choose_an_answer))
                } else {
                    setSettingsDialog(checkAnswer == answerString, checkAnswer, task.taskNum)
                    bottomSheet.visibility = View.VISIBLE
                    accessibilityButtons(contentLayout, false)
                }
            }
            itemMissingWord.variantOne.text = task.variants.split(" ")[0]
            itemMissingWord.variantTwo.text = task.variants.split(" ")[1]
            exerciseInfo.text = task.question
        }
    }

    private fun bindForTranslate(task: TaskData) {
        with(binding) {
            description.text = getString(R.string.translate_the_sentence)
            itemTranclate.root.visibility = View.VISIBLE
            exerciseInfo.visibility = View.VISIBLE

            val checkAnswer = task.answer
            exerciseInfo.text = task.question

            check.setOnClickListener {
                val answerString = itemTranclate.answerBox.children
                    .filterIsInstance<CardView>()
                    .flatMap { cardView -> cardView.getAllTextViews() }
                    .joinToString(" ") { textView -> textView.text }
                setSettingsDialog(checkAnswer == answerString, checkAnswer, task.taskNum)
                bottomSheet.visibility = View.VISIBLE
                accessibilityButtons(contentLayout, false)
            }
            task.answer.split(" ").plus(task.variants.split(" "))
                .forEach { addTextViewToOptionAndAnswer(it, task.typeTask) }
        }
    }

    private fun addTextViewToOptionAndAnswer(text: String, typeTask: Int) {
        val cardView = CardView(requireContext())
        val textView = TextView(requireContext())
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textView.setTextColor(resources.getColor(R.color.black, null))
        textView.setPadding(16, 16, 16, 16)
        cardView.addView(textView)
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        cardView.layoutParams = layoutParams
        cardView.setCardBackgroundColor(resources.getColor(R.color.white, null))
        cardView.cardElevation = 8f
        cardView.radius = 16f


        cardView.setOnClickListener {
            if (typeTask == 1 || typeTask == 4) {
                speak(text)
            }
            if (cardView.parent == binding.itemTranclate.optionBox) {
                binding.itemTranclate.optionBox.removeView(cardView)
                binding.itemTranclate.answerBox.addView(cardView)
            } else {
                binding.itemTranclate.answerBox.removeView(cardView)
                binding.itemTranclate.optionBox.addView(cardView)
            }
        }
        if (binding.itemTranclate.optionBox.childCount < 50) {
            binding.itemTranclate.optionBox.addView(cardView)
        } else {
            showToast(getString(R.string.you_have_reached_the_maximum_number_of_options))
        }
    }

    private fun setSettingsDialog(answerIsTrue: Boolean, answer: String, numTask: Int) {
        with(binding.dialogResultId) {
            val bgColor = if (answerIsTrue) R.color.green_lite else R.color.red_lite
            val buttonBgColor = if (answerIsTrue) R.color.green else R.color.red
            val textColor = if (answerIsTrue) R.color.green else R.color.red
            val imageAnswer = if (answerIsTrue) R.drawable.ic_done else R.drawable.ic_close
            when {
                answerIsTrue -> {
                    if (workWithList) {
                        listErrors.removeAt(0)
                    }
                    if (args.isMultiplayer) {
                        correctAnswersCount += 1
                    }
                    viewModel.addProgress()
                    resultAnswer.text = getString(R.string.success)
                }
                else -> {
                    if (!workWithList) {
                        listErrors.add(numTask)
                    }
                    resultAnswer.text = getString(R.string.error)
                    trueAnswerText.text = getString(R.string.correct_answer)
                }
            }
            imageResult.setImageResource(imageAnswer)
            dialogResult.setBackgroundColor(resources.getColor(bgColor, null))
            buttonContinue.setBackgroundColor(resources.getColor(buttonBgColor, null))
            buttonContinue.setTextColor(resources.getColor(R.color.white, null))
            resultAnswer.setTextColor(resources.getColor(textColor, null))
            textAnswer.setTextColor(resources.getColor(textColor, null))
            trueAnswerText.setTextColor(resources.getColor(textColor, null))
            textAnswer.text = answer
        }
    }

    private fun accessibilityButtons(layout: ViewGroup, accessibility: Boolean) {
        layout.descendants.filterIsInstance<Button>().forEach { button ->
            button.isClickable = accessibility
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showConfirmationDialog() {
        ConfirmationDialogUtils.showConfirmationDialog(
            requireContext(),
            findNavController()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    override fun onDestroy() {
        if (args.isMultiplayer) {
            viewModel.saveAnswers(
                args.gameCode,
                correctAnswersCount,
                startTime,
                isComplete = true,
                isForDelete = true
            )
        }
        super.onDestroy()

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