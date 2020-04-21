package com.ake.ewhanoticeclient.activity_subscribe

import com.ake.ewhanoticeclient.domain.Board

class BoardClickListener(val clickListener: (board: Board) -> Unit){
    fun onClick(board: Board) = clickListener(board)
}