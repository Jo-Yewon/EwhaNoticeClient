package com.ake.ewhanoticeclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.Board
import kotlinx.android.synthetic.main.board_selected.view.*

class SelectedBoardAdapter(
    private var selectedBoardsList: MutableList<Board>
): RecyclerView.Adapter<SelectedBoardAdapter.SelectedBoardViewHolder>() {

    lateinit var boardAdapter: BoardAdapter

    class SelectedBoardViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.board_selected,
                parent, false
            )
        ) {
        val title = itemView.tv_selected_board_title
        val buttonDelete = itemView.bt_delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedBoardViewHolder =
        SelectedBoardViewHolder(parent)

    override fun onBindViewHolder(holder: SelectedBoardViewHolder, position: Int) {
        holder.title.text = selectedBoardsList[position].title
        holder.buttonDelete.setOnClickListener{
            boardAdapter.addItem(selectedBoardsList[position])
            deleteItem(position)
        }
    }

    override fun getItemCount() = selectedBoardsList.size

    fun addItem(board:Board){
        selectedBoardsList.add(board)
        notifyItemInserted(itemCount-1)
    }

    private fun deleteItem(position: Int){
        selectedBoardsList.removeAt(position)
        notifyDataSetChanged()
    }
}
