package com.rai.powereng.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rai.powereng.databinding.ItemRatingBinding
import com.rai.powereng.model.UserScoreWithProfile

class UsersRatingAdapter(context: Context): ListAdapter<UserScoreWithProfile, UsersScoreViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersScoreViewHolder {
        return UsersScoreViewHolder(
            binding = ItemRatingBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun submitList(list: List<UserScoreWithProfile>?) {
        // Sort the list by score in descending order before submitting
        val sortedList = list?.sortedByDescending { it.score }
        super.submitList(sortedList)
    }

    override fun onBindViewHolder(holder: UsersScoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<UserScoreWithProfile>() {
            override fun areItemsTheSame(
                oldItem: UserScoreWithProfile,
                newItem: UserScoreWithProfile,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserScoreWithProfile,
                newItem: UserScoreWithProfile,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class UsersScoreViewHolder(
    private val binding: ItemRatingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: UserScoreWithProfile, position: Int) {
        binding.userName.text = item.displayName
        binding.userNum.text = (position +1).toString() +"."
        binding.userScore.text = item.score.toString() + " xp"
        binding.userPhoto.load(item.photoUrl)
    }
}