package com.greentea.storyapp2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentea.storyapp2.services.models.StoryResult
import com.greentea.storyapp2.databinding.ItemRowStoryBinding

class ListStoryUsersAdapter(
    val context: Context
) : RecyclerView.Adapter<ListStoryUsersAdapter.ListUsersViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<StoryResult>(){
        override fun areItemsTheSame(
            oldItem: StoryResult,
            newItem: StoryResult
        ): Boolean {
            //COMPARE ID BECAUSE IT'S UNIQUE
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoryResult,
            newItem: StoryResult
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differAsync = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((StoryResult) -> Unit)? = null

    inner class ListUsersViewHolder(var bindingListUsersAdapter: ItemRowStoryBinding): RecyclerView
    .ViewHolder(bindingListUsersAdapter.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersViewHolder {
        val bindingListUsersAdapter = ItemRowStoryBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return ListUsersViewHolder(bindingListUsersAdapter)
    }

    override fun onBindViewHolder(holder: ListUsersViewHolder, position: Int) {
        val user = differAsync.currentList[position]

        holder.itemView.apply {
            Glide.with(this)
                .load(user.photoUrl)
                .into(holder.bindingListUsersAdapter.ivPhoto)
            holder.bindingListUsersAdapter.tvItemName.text = user.name
            holder.bindingListUsersAdapter.tvItemDate.text = user.createdAt

            setOnClickListener {
                onItemClickListener?.let {
                    it(user) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }

    fun setOnItemClickListener(listener: (StoryResult) -> Unit){
        onItemClickListener = listener
    }

}