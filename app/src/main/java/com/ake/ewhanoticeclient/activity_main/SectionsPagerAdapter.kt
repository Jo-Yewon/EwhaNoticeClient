package com.ake.ewhanoticeclient.activity_main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ake.ewhanoticeclient.database.BoardRepository
import com.ake.ewhanoticeclient.network.ServerApi

class SectionsPagerAdapter(
    fm: FragmentManager,
    boardRepository: BoardRepository
) : FragmentPagerAdapter(fm) {

    private val subscribedBoards by lazy { boardRepository.getSubscribedBoardList() }
    private val apiService by lazy { ServerApi.create() }

    override fun getItem(position: Int): Fragment =
        subscribedBoards[position].let{
            if (it.boardId == 13259) CommonPlaceholderFragment.newInstance()
            else PlaceholderFragment.newInstance(it, apiService) }

    override fun getPageTitle(position: Int) = subscribedBoards[position].alias

    override fun getCount() = subscribedBoards.size

    override fun getItemPosition(`object`: Any): Int
    {
        return PagerAdapter.POSITION_NONE
    }
}