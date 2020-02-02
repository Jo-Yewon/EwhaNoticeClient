package com.ake.ewhanoticeclient.activity_main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ake.ewhanoticeclient.database.BoardRepository

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    fm: FragmentManager,
    boardRepository: BoardRepository
) :
    FragmentPagerAdapter(fm) {

    private val subscribedBoards by lazy { boardRepository.getSubscribedBoardList() }

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(subscribedBoards[position])
    }

    override fun getPageTitle(position: Int) = subscribedBoards[position].alias

    override fun getCount() = subscribedBoards.size
}