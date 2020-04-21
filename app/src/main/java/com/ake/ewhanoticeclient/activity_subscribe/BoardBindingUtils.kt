package com.ake.ewhanoticeclient.activity_subscribe

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.domain.Board

@BindingAdapter("boardCategoryAndTitle")
fun TextView.setBoardCategoryAndTitle(item: Board){
    text = "${item.boardCategory} > ${item.title}"
}

@BindingAdapter("bottomBoardListData")
fun bindBottomBoardsRecyclerView(recyclerView: RecyclerView, data: List<Board>?){
    val adapter = recyclerView.adapter as BottomBoardsAdapter
    adapter.submitList(data)
}

@BindingAdapter("subscribedBoardListData")
fun bindSubscribedBoardsRecyclerView(recyclerView: RecyclerView, data: List<Board>?){
    val adapter = recyclerView.adapter as SubscribedBoardsAdapter
    adapter.submitList(data)
}