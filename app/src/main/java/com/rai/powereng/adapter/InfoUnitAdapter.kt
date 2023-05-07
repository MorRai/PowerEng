package com.rai.powereng.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rai.powereng.databinding.ItemUnitInfoBinding
import com.rai.powereng.model.TaskData

class InfoUnitAdapter(context: Context): ListAdapter<TaskData, InfoUnitViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoUnitViewHolder {
        return InfoUnitViewHolder(
            binding = ItemUnitInfoBinding.inflate(layoutInflater, parent, false)
        )
    }


    override fun onBindViewHolder(holder: InfoUnitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<TaskData>() {
            override fun areItemsTheSame(
                oldItem: TaskData,
                newItem: TaskData,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TaskData,
                newItem: TaskData,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class InfoUnitViewHolder(
    private val binding: ItemUnitInfoBinding,
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: TaskData) {
        binding.phraseTextView.text = item.forInfoUnit
    }
}