package com.quiraadev.storyapp.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quiraadev.storyapp.data.source.remote.response.StoryItem
import com.quiraadev.storyapp.databinding.ItemStoryBinding

class StoryAdapter(
    private val stories: List<StoryItem>,
    private val onClick: ((id: String) -> Unit)?
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(story: StoryItem) {

            binding.apply {
                tvItemName.text = story.name
                binding.ivItemPhoto.load(story.photoUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun getItemCount(): Int = stories.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = stories[position]
        holder.bindView(data)
        holder.binding.cardStory.setOnClickListener {
            onClick?.invoke(data.id)
        }
    }

}