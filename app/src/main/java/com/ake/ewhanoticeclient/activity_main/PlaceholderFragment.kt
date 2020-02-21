package com.ake.ewhanoticeclient.activity_main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.databinding.FragmentNoticesBinding
import com.ake.ewhanoticeclient.network.ServerApi
import android.content.Intent
import android.webkit.URLUtil
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager


class PlaceholderFragment(
    private val board: Board,
    private val apiService: ServerApi
) : Fragment() {

    private lateinit var pageViewModel: NoticePageViewModel
    private lateinit var binding: FragmentNoticesBinding

    private lateinit var customTabsIntent: CustomTabsIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = NoticePageViewModelFactory(board, apiService)
        pageViewModel = ViewModelProviders.of(this, factory)
            .get(NoticePageViewModel::class.java)

        initCustomTabs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notices, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = pageViewModel

        val noticesAdapter = NoticesAdapter(NoticeClickListener { pageViewModel.showNotice(it) },
            HeaderFooterClickListener { pageViewModel.expandBoard() })
        binding.noticesRecyclerView.adapter = noticesAdapter

        pageViewModel.notices.observe(viewLifecycleOwner, Observer {
            it?.let { noticesAdapter.submitList(it) }
        })

        // Show notice with ctt
        pageViewModel.url.observe(viewLifecycleOwner, Observer {
            it?.let {
                pageViewModel.endShowNotice()
                customTabsIntent.launchUrl(context!!, Uri.parse(it))
            }
        })

        // Show web site with intent
        pageViewModel.expandBoard.observe(viewLifecycleOwner, Observer {
            it?.let {
                pageViewModel.endExpand()
                if (URLUtil.isValidUrl(it))
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                else
                    Toast.makeText(context, "웹 사이트로 이동할 수 없습니다.", Toast.LENGTH_SHORT)
            }
        })

        //Show toast message
        pageViewModel.toast.observe(viewLifecycleOwner, Observer {
            it?.let {
                pageViewModel.endToast()
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        //Scroll top
        pageViewModel.scrollTop.observe(viewLifecycleOwner, Observer {
            if (it){
                pageViewModel.endScrollTop()
                (binding.noticesRecyclerView.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(0, 0)
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(board: Board, apiService: ServerApi): PlaceholderFragment {
            return PlaceholderFragment(board, apiService)
        }
    }

    private fun initCustomTabs() {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(resources.getColor(R.color.colorPrimaryDark))
        customTabsIntent = builder.build()
    }
}
