package com.rai.powereng.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rai.powereng.R
import com.rai.powereng.TaskAvailability
import com.rai.powereng.databinding.ItemUnitBinding
import com.rai.powereng.model.UnitData
import com.rai.powereng.model.UserScore
import com.rai.powereng.ui.tabs.unitsList.PartClickListener


class UnitsAdapter(
    context: Context,
    private val partListener: PartClickListener,
    private val onItemClicked: (UnitData) -> Unit,
) : ListAdapter<UnitData, UnitViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    private var userScore: UserScore? = null
    @SuppressLint("NotifyDataSetChanged")
    fun setScore(score: UserScore?) {
        userScore = score
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        return UnitViewHolder(
            binding = ItemUnitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, partListener, userScore,onItemClicked)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<UnitData>() {
            override fun areItemsTheSame(
                oldItem: UnitData,
                newItem: UnitData,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UnitData,
                newItem: UnitData,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}


class UnitViewHolder(
    private val binding: ItemUnitBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val partViews =
        arrayOf(binding.part1, binding.part2, binding.part3, binding.part4, binding.part5)
    private val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    private val colorFilter = ColorMatrixColorFilter(colorMatrix)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: UnitData, partListener: PartClickListener, userScore: UserScore?,onItemClicked: (UnitData) -> Unit) {
        val maxUnit = userScore?.unit ?: 1
        val maxPart = userScore?.part?.plus(1) ?: 1
        binding.unitDescription.text = item.description
        binding.unitNumber.text = "Unit ${item.unitId}"
        binding.unitInfo.setOnClickListener {
            onItemClicked(item)
        }

        val touchListener = View.OnTouchListener { view, motionEvent ->
            val partIndex = partViews.indexOf(view)
            when (motionEvent.action) {

                MotionEvent.ACTION_DOWN -> {
                    //val partIndex = partViews.indexOf(view)
                    if (view is ImageView) {
                        when (getTaskAvailability(item.unitId, partIndex + 1, maxUnit, maxPart)){
                            TaskAvailability.ACTIVE -> view.setImageResource(R.drawable.active_pressed)
                            TaskAvailability.COMPLETE -> view.setImageResource(R.drawable.complete_not_pressed)
                            TaskAvailability.BLOCK -> view.setImageResource(R.drawable.block_not_pressed)
                        }

                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (view is ImageView) {
                        when (getTaskAvailability(item.unitId, partIndex + 1, maxUnit, maxPart)){
                            TaskAvailability.ACTIVE -> view.setImageResource(R.drawable.active_not_pressed)
                            TaskAvailability.COMPLETE -> view.setImageResource(R.drawable.complete_not_pressed)
                            TaskAvailability.BLOCK -> view.setImageResource(R.drawable.block_not_pressed)
                        }
                    }
                    //val partIndex = partViews.indexOf(view)
                    if (partIndex >= 0 && isEnabled(item.unitId, partIndex + 1, maxUnit, maxPart)) {
                        partListener.onPartClickListener(
                            item.unitId,
                            partIndex + 1,
                            adapterPosition,
                            view
                        )
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (view is ImageView) {
                        when (getTaskAvailability(item.unitId, partIndex + 1, maxUnit, maxPart)){
                            TaskAvailability.ACTIVE -> view.setImageResource(R.drawable.active_not_pressed)
                            TaskAvailability.COMPLETE -> view.setImageResource(R.drawable.complete_not_pressed)
                            TaskAvailability.BLOCK -> view.setImageResource(R.drawable.block_not_pressed)
                        }
                    }
                    true
                }
                else -> false
            }


        }
        partViews.forEachIndexed { index, partView ->
            val taskAvailability = getTaskAvailability(item.unitId, index + 1, maxUnit, maxPart)
            val initialImage = when (taskAvailability) {
                TaskAvailability.ACTIVE -> R.drawable.active_not_pressed
                TaskAvailability.COMPLETE -> R.drawable.complete_not_pressed
                TaskAvailability.BLOCK -> R.drawable.block_not_pressed
            }
            partView.setImageResource(initialImage)
            setTouchListenerOnPart(
                partView,
                taskAvailability,
                touchListener,
                colorFilter
            )
        }
    }

    private fun setTouchListenerOnPart(
        partView: ImageView,
        taskAvailability: TaskAvailability,
        touchListener: View.OnTouchListener,
        colorFilter: ColorFilter,
    ) {
        partView.apply {
            setOnTouchListener(touchListener)
            if (taskAvailability == TaskAvailability.BLOCK) {
                setColorFilter(colorFilter)
            }else{
                setColorFilter(null)
            }
        }
    }

    private fun isEnabled(unitId: Int, part: Int, maxUnit: Int, maxPart: Int): Boolean {
        return maxUnit > unitId || (maxUnit == unitId && maxPart >= part)
    }


    private fun getTaskAvailability(unitId: Int, part: Int, maxUnit: Int, maxPart: Int): TaskAvailability {
        return when {
            maxUnit > unitId -> TaskAvailability.COMPLETE
            maxUnit == unitId && maxPart > part -> TaskAvailability.COMPLETE
            maxUnit == unitId && maxPart == part -> TaskAvailability.ACTIVE
            else -> TaskAvailability.BLOCK
        }
    }
}

