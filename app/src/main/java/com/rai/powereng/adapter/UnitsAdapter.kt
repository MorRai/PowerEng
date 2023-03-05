package com.rai.powereng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rai.powereng.databinding.ItemUnitBinding
import com.rai.powereng.model.UnitData


class UnitsAdapter(
    context: Context,
    private val onItemClicked: (UnitData) -> Unit,
) : ListAdapter<UnitData, UnitViewHolder>(DIFF_UTIL) {


    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        return UnitViewHolder(
            binding = ItemUnitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,onItemClicked)
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

    fun bind(item: UnitData, onItemClicked: (UnitData) -> Unit) {
        //binding.imageCard.load(item.image)
        //binding.nameClass.text = item.name
        //itemView.setOnClickListener {
            //onItemClicked(item)
       // }
    }

}

