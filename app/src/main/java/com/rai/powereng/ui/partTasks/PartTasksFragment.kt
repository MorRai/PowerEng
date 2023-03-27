package com.rai.powereng.ui.partTasks

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import java.util.*

class PartTasksFragment : Fragment(){
    private var _binding: FragmentPartTasksBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private var tts: TextToSpeech? = null

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
       // tts = TextToSpeech(requireContext(), this)
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

    }


    private fun clearForm() {
        with(binding) {
            itemTranclate.answerBox.removeAllViews()
            itemTranclate.optionBox.removeAllViews()
        }
        viewModel.getTasksUser(args.unitId, args.partId, taskNum)
    }

    private fun bind(task: TaskData) {
        with(binding) {
            if (task.typeTask == 1 || task.typeTask == 2) {
                description.text = "Переведите предложение:"
                itemTranclate.root.visibility = View.VISIBLE
                itemMissingWord.root.visibility = View.GONE
                itemListen.root.visibility = View.GONE
                bindForTranslate(task)
            } else if (task.typeTask == 3) {
                description.text = "Вставьте слово"
                itemTranclate.root.visibility = View.GONE
                itemMissingWord.root.visibility = View.VISIBLE
                itemListen.root.visibility = View.GONE
                bindForMissingWord(task)
            }
            else if (task.typeTask == 4) {
                description.text = "Введите что услышали"
                itemTranclate.root.visibility = View.GONE
                itemMissingWord.root.visibility = View.GONE
                itemListen.root.visibility = View.VISIBLE
                bindForListen(task)
            }
        }
    }


    private fun bindForListen(task: TaskData) {
        with(binding){
            val textForListen = task.answer
            itemListen.playSound.setOnClickListener {
                speak(textForListen)
            }
            itemListen.playSoundSlow.setOnClickListener {
                speak(textForListen,0.5f)
            }

            check.setOnClickListener {
                var answerString = ""
                for (childView in itemListen.answerBox.children) {
                    if (childView is Button) {
                        answerString = answerString + " " + childView.text
                    }
                }
                answerString = answerString.drop(1)
                if (textForListen == answerString) {
                    Toast.makeText(requireContext(), "успех", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "провал", Toast.LENGTH_SHORT).show()
                }
                taskNum += 1
                clearForm()

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
            var radioAnswer = ""
            check.setOnClickListener {
                when (itemMissingWord.wordVariants.checkedRadioButtonId) {
                    R.id.variantOne -> radioAnswer = itemMissingWord.variantOne.text.toString()
                    R.id.variantTwo -> radioAnswer = itemMissingWord.variantTwo.text.toString()
                }
                if (radioAnswer == "") {
                    Toast.makeText(requireContext(), "Хули не выбрано ничего", Toast.LENGTH_SHORT)
                        .show()
                } else if (checkAnswer == radioAnswer) {
                    Toast.makeText(requireContext(), "успех", Toast.LENGTH_SHORT).show()
                    taskNum += 1
                    clearForm()
                } else {
                    Toast.makeText(requireContext(), "провал", Toast.LENGTH_SHORT).show()
                    taskNum += 1
                    clearForm()
                }
            }
            val listWords = task.variants.split(" ")//их всегда 2
            itemMissingWord.variantOne.text = listWords[0]
            itemMissingWord.variantTwo.text = listWords[1]
            itemMissingWord.exerciseInfo.text = task.question
        }
    }

    private fun bindForTranslate(task: TaskData) {
        with(binding) {
            val checkAnswer = task.answer
            itemTranclate.exerciseInfo.text = task.question
            check.setOnClickListener {
                var answerString = ""
                for (childView in itemTranclate.answerBox.children) {
                    if (childView is Button) {
                        answerString = answerString + " " + childView.text
                    }
                }
                answerString = answerString.drop(1)
                if (checkAnswer == answerString) {
                    Toast.makeText(requireContext(), "успех", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "провал", Toast.LENGTH_SHORT).show()
                }
                taskNum += 1
                clearForm()

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
        textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            if (typeTask == 1 || typeTask == 4) {
                speak(text)
            }
            addTextViewToAnswer(text,typeTask)
            binding.itemTranclate.optionBox.removeView(textView)
        }
        binding.itemTranclate.optionBox.addView(textView)
    }

    private fun addTextViewToAnswer(text: String, typeTask: Int) {
        val textView = Button(requireContext())
        textView.text = text
        textView.setPadding(16, 16, 16, 16)
        textView.setTextAppearance(R.style.FlexItem)
        textView.setOnClickListener {
            addTextViewToOption(text,typeTask)
            binding.itemTranclate.answerBox.removeView(textView)
        }
        binding.itemTranclate.answerBox.addView(textView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
    }


    private fun speak(text: String, speed:Float = 1.0f) {
        tts = TextToSpeech(requireContext(),TextToSpeech.OnInitListener {
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
        })
    }

}