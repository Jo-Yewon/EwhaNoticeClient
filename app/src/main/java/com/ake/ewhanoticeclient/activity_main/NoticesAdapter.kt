package com.ake.ewhanoticeclient.activity_main

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.databinding.NoticeFooterBinding
import com.ake.ewhanoticeclient.databinding.NoticeItemBinding
import com.ake.ewhanoticeclient.network.Notice
import java.lang.Exception

class NoticesAdapter(
    private val clickListener: NoticeClickListener,
    private val footerClickListener: FooterClickListener
) :
    PagedListAdapter<Notice, RecyclerView.ViewHolder>(NoticeDiffCallback()) {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_FOOTER = 1
    }

    override fun getItemViewType(position: Int) =
        if (position == itemCount - 1) TYPE_FOOTER else TYPE_ITEM

    override fun getItemCount() = super.getItemCount() + 1 //For footer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = when (viewType) {
            TYPE_ITEM -> NoticeViewHolder.from(parent)
            else -> FooterViewHolder.from(parent, footerClickListener)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoticeViewHolder) {
            val res = holder.itemView.context.resources
            val item = getItem(position)
            item?.let {
                holder.bind(item, res, clickListener)
            }
        }
    }

    class FooterViewHolder(binding: NoticeFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup, clickListener: FooterClickListener): FooterViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoticeFooterBinding
                    .inflate(layoutInflater, parent, false)
                binding.clickListener = clickListener
                return FooterViewHolder(binding)
            }
        }
    }

    class NoticeViewHolder(private val binding: NoticeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notice, res: Resources, clickListener: NoticeClickListener) {
            binding.notice = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): NoticeViewHolder {
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