package com.ake.ewhanoticeclient.activity_subscribe

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ake.ewhanoticeclient.database.Board

@BindingAdapter("boardCategoryAndTitle")
fun TextView.setBoardCategoryAndTitle(item: Board){ text = "${item.boardCategory} > ${item.title}"}