package com.dicoding.midsubmissionintermediate.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.midsubmissionintermediate.data.remote.response.ListStoryItem
import com.dicoding.midsubmissionintermediate.databinding.StoriesRowBinding
import com.dicoding.midsubmissionintermediate.view.detail.DetailActivity
import com.dicoding.midsubmissionintermediate.view.detail.DetailActivity.Companion.KEY_DETAIL
import androidx.core.util.Pair

class StoriesAdapter : ListAdapter<ListStoryItem, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoriesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(
        private val binding: StoriesRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ListStoryItem) {
            with(binding){
                tvNameStory.text = user.name
                tvDescriptionStory.text = user.description
                Glide.with(root.context)
                    .load(user.photoUrl)
                    .into(ivStory)

                    root.setOnClickListener {
                    val intent = Intent(root.context, DetailActivity::class.java)
                    intent.putExtra(KEY_DETAIL, user)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(ivStory, "photo"),
                            Pair(tvNameStory, "name"),
                            Pair(tvDescriptionStory, "desc"),
                        )
                    root.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}