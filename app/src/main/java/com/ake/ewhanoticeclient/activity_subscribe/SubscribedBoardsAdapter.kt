package com.ake.ewhanoticeclient.activity_subscribe

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.databinding.SubscribedBoardItemBinding
import com.ake.ewhanoticeclient.domain.Board

class SubscribedBoardsAdapter(private val clickListener: BoardClickListener) :
    ListAdapter<Board, SubscribedBoardsAdapter.SubscribedBoardViewHolder>(SubscribedBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribedBoardViewHolder =
        SubscribedBoardViewHolder.from(parent)

    override fun onBindViewHolder(holder: SubscribedBoardViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        val item = getItem(position)
        holder.bind(item, res, clickListener)
    }

    class SubscribedBoardDiffCallback : DiffUtil.ItemCallback<Board>() {
        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.boardId == newItem.boardId
        }
    }

    class SubscribedBoardViewHolder(private val binding: SubscribedBoardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Board, res: Resources, clickListener: BoardClickListener) {
            binding.board = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): SubscribedBoardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SubscribedBoardItemBinding
                    .inflate(layoutInflater, parent, false)
                return SubscribedBoardViewHolder(binding)
            }
        }
    }
}
