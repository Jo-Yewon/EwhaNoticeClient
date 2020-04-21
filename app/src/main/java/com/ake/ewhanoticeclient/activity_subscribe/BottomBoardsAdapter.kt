package com.ake.ewhanoticeclient.activity_subscribe

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.databinding.BoardItemBinding
import com.ake.ewhanoticeclient.domain.Board


class BottomBoardsAdapter(val clickListener: BoardClickListener):
    ListAdapter<Board, BottomBoardsAdapter.BoardViewHolder>(BoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder =
        BoardViewHolder.from(parent)

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        val item = getItem(position)
        holder.bind(item, res, clickListener)
    }

    class BoardDiffCallback : DiffUtil.ItemCallback<Board>(){
        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.boardId == newItem.boardId
        }
    }

    class BoardViewHolder(private val binding: BoardItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: Board, res: Resources, clickListener: BoardClickListener){
            binding.board = item
            binding.clickListener = clickListener
        }

        companion object{
            fun from(parent: ViewGroup): BoardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BoardItemBinding
                    .inflate(layoutInflater, parent, false)
                return BoardViewHolder(
                    binding
                )
            }
        }
    }
}
