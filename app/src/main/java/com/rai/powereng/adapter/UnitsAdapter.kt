package com.rai.powereng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rai.powereng.databinding.ItemUnitBinding
import com.rai.powereng.model.UnitData
import com.rai.powereng.ui.unitsList.PartClickListener


class UnitsAdapter(
    context: Context,
    private val partListener: PartClickListener
   // private val onItemClicked: (UnitData) -> Unit,
) : ListAdapter<UnitData, UnitViewHolder>(DIFF_UTIL) {

   //var ? = null

    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        return UnitViewHolder(
            binding = ItemUnitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,partListener)
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

    fun bind(item: UnitData, partListener: PartClickListener) {
        binding.unitDescription.text = item.description
        binding.unitNumber.text = "Unit ${item.unitId}"  //покуда тест
        binding.part1.setOnClickListener {
            partListener.onPartClickListener(item.unitId,1)
        }
        binding.part2.setOnClickListener {
            partListener.onPartClickListener(item.unitId,2)
        }
        binding.part3.setOnClickListener {
            partListener.onPartClickListener(item.unitId,3)
        }
        binding.part4.setOnClickListener {
            partListener.onPartClickListener(item.unitId,4)
        }
        binding.part5.setOnClickListener {
            partListener.onPartClickListener(item.unitId,5)
        }

    }

}

