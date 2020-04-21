package com.ake.ewhanoticeclient.activity_main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ake.ewhanoticeclient.repositories.BoardRepository

class SectionsPagerAdapter(
    fm: FragmentManager,
    l: Lifecycle,
    boardRepository: BoardRepository
) : FragmentStateAdapter(fm, l) {

    private val subscribedBoards by lazy { boardRepository.getSubscribedBoardList() }

    fun getItemAlias(position: Int) = subscribedBoards[position].alias

    override fun getItemCount() = subscribedBoards.size

    override fun createFragment(position: Int): Fragment =
        subscribedBoards[position].let{
            if (it.boardId == 13259) CommonPlaceholderFragment.newInstance()
            else PlaceholderFragment.newInstance(it) }
}