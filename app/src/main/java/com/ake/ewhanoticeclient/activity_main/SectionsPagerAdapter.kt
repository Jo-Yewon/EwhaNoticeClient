package com.ake.ewhanoticeclient.activity_main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ake.ewhanoticeclient.database.BoardRepository

class SectionsPagerAdapter(
    fm: FragmentManager,
    boardRepository: BoardRepository
) : FragmentPagerAdapter(fm) {

    private val subscribedBoards by lazy { boardRepository.getSubscribedBoardList() }

    override fun getItem(position: Int): Fragment =
        PlaceholderFragment.newInstance(subscribedBoards[position])

    override fun getPageTitle(position: Int) = subscribedBoards[position].alias

    override fun getCount() = subscribedBoards.size
}