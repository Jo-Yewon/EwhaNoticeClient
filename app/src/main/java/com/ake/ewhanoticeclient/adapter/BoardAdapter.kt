package com.ake.ewhanoticeclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.Board
import kotlinx.android.synthetic.main.board_item.view.*

class BoardAdapter(private val boardData: MutableList<Board>):
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>(){

    lateinit var seletedBoardAdapter: SelectedBoardAdapter

    class BoardViewHolder(parent: ViewGroup):
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)){
        val title = itemView.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder =
        BoardViewHolder(parent)

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.title.text = boardData[position].title
        holder.itemView.setOnClickListener{
            seletedBoardAdapter.addItem(boardData[position])
            deleteItem(position)
        }
    }

    override fun getItemCount() = boardData.size

    fun addItem(board: Board){
        boardData.add(board)
        notifyItemInserted(itemCount-1)
    }

    private fun deleteItem(position: Int){
        boardData.removeAt(position)
        notifyDataSetChanged()
    }
}