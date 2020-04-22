package com.ake.ewhanoticeclient.activity_subscribe

import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.domain.Board
import kotlin.random.Random

@BindingAdapter("boardCategoryAndTitle")
fun TextView.setBoardCategoryAndTitle(item: Board){
    text = "${item.boardCategoryKorean} > ${item.title}"
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
    if (data != null && data.isNotEmpty())
        recyclerView.smoothScrollToPosition(data.size-1)
}

@BindingAdapter("visibilityAnim")
fun bindVisibilityWithAnim(button: Button, view: Int){
    val animation = when(view){
        View.GONE -> AlphaAnimation(1f, 0f)
        else -> AlphaAnimation(0f, 1f)
    }
    animation.duration = 1000
    button.visibility = view
    button.animation = animation
}