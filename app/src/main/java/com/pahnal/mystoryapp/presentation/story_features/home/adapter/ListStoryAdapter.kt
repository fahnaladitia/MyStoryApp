package com.pahnal.mystoryapp.presentation.story_features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ItemStoryBinding
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.utils.convertTimeStampToDisplay

class ListStoryAdapter : PagingDataAdapter<Story, ListStoryAdapter.ListStoryViewHolder>(
    DIFF_CALLBACK
) {
    var onStoryClickListener: ((story: Story, binding: ItemStoryBinding) -> Unit)? = null

    inner class ListStoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView.context).load(story.photoUrl).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .error(R.drawable.ic_baseline_broken_image_24).into(storyPhoto)

                storyDate.text = story.createdAt.convertTimeStampToDisplay()
                storyName.text = story.name
                storyDescription.text = story.description
            }
            binding.root.setOnClickListener {
                onStoryClickListener?.invoke(story, binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        return ListStoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story, newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story, newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
