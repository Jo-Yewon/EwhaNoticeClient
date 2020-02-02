package com.ake.ewhanoticeclient.activity_main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.Board

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(
    private val board: Board
) : Fragment() {

    private lateinit var pageViewModel: NoticePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = NoticePageViewModelFactory(board)
        pageViewModel = ViewModelProviders.of(this, factory)
            .get(NoticePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.boardRecyclerView) as RecyclerView
        val adapter = NoticesAdapter(NoticeClickListener{ Log.d("pass", "pass") })
        recyclerView.adapter = adapter

        pageViewModel.notices.observe(this, Observer {
            it?.let { adapter.submitList(it)}
        })
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(board: Board): PlaceholderFragment {
            return PlaceholderFragment(board)
        }
    }
}
