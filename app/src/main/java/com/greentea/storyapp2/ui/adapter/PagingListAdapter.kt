package com.greentea.storyapp2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentea.storyapp2.databinding.ItemRowStoryBinding
import com.greentea.storyapp2.services.models.StoryResult

class PagingListAdapter :
    PagingDataAdapter<StoryResult, PagingListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((StoryResult) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.itemView.apply {
                Glide.with(this)
                    .load(data.photoUrl)
                    .into(holder.binding.ivPhoto)
                holder.binding.tvItemName.text = data.name
                holder.binding.tvItemDate.text = data.createdAt

                setOnClickListener {
                    onItemClickListener?.let {
                        it(data) }
                }
            }
        }
    }

    class MyViewHolder(val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (StoryResult) -> Unit){
        onItemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResult>() {
            override fun areItemsTheSame(oldItem: StoryResult, newItem: StoryResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResult, newItem: StoryResult): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}