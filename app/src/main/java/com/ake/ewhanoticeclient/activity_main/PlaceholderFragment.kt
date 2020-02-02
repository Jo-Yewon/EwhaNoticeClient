package com.ake.ewhanoticeclient.activity_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.databinding.FragmentNoticesBinding

class PlaceholderFragment(private val board: Board) : Fragment() {

    private lateinit var pageViewModel: NoticePageViewModel
    private lateinit var binding: FragmentNoticesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = NoticePageViewModelFactory(board)
        pageViewModel = ViewModelProviders.of(this, factory)
            .get(NoticePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notices, container, false)

        val noticesAdapter = NoticesAdapter(NoticeClickListener { pageViewModel.showNotice(it) })
        binding.noticesRecyclerView.adapter = noticesAdapter
        pageViewModel.notices.observe(this, Observer {
            it?.let { noticesAdapter.submitList(it) }
        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(board: Board): PlaceholderFragment {
            return PlaceholderFragment(board)
        }
    }
}
