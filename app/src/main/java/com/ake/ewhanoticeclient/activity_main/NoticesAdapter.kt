package com.ake.ewhanoticeclient.activity_main

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.databinding.NoticeItemBinding
import com.ake.ewhanoticeclient.network.Notice

class NoticesAdapter(private val clickListener: NoticeClickListener) :
    PagedListAdapter<Notice, NoticesAdapter.NoticeViewHolder>(NoticeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder =
        NoticeViewHolder.from(parent)

    override fun onBindViewHolder(holder: NoticesAdapter.NoticeViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        val item = getItem(position)
        item?.let { holder.bind(item, res, clickListener) }
    }

    class NoticeViewHolder(private val binding: NoticeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notice, res: Resources, clickListener: NoticeClickListener){
            binding.notice = item
            binding.clickListener = clickListener
        }

        companion object{
            fun from(parent: ViewGroup): NoticeViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoticeItemBinding
                    .inflate(layoutInflater, parent, false)
                return NoticeViewHolder(binding)
            }
        }

    }

    class NoticeDiffCallback : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem.num == newItem.num
        }

        override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem.category == newItem.category
                    && oldItem.date == newItem.date
                    && oldItem.title == newItem.title
                    && oldItem.url == newItem.url
        }
    }
}