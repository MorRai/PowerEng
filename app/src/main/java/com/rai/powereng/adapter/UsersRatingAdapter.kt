package com.rai.powereng.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rai.powereng.databinding.ItemRatingBinding
import com.rai.powereng.model.UserScore

class UsersRatingAdapter(context: Context): ListAdapter<UserScore, UsersScoreViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersScoreViewHolder {
        return UsersScoreViewHolder(
            binding = ItemRatingBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersScoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }


    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<UserScore>() {
            override fun areItemsTheSame(
                oldItem: UserScore,
                newItem: UserScore,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserScore,
                newItem: UserScore,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}

class UsersScoreViewHolder(
    private val binding: ItemRatingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserScore, position: Int) {
        binding.userName.text = item.userId
        binding.userNum.text = position.toString()
        binding.userScore.text = item.score.toString()
    }
}