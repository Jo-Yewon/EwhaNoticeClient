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
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.databinding.FragmentNoticesBinding
import android.content.Intent
import android.webkit.URLUtil
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.domain.Board


class PlaceholderFragment: Fragment() {

    private lateinit var pageViewModel: NoticePageViewModel
    private lateinit var binding: FragmentNoticesBinding

    private lateinit var customTabsIntent: CustomTabsIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val board = arguments?.getParcelable<Board>("board")

        val factory = NoticePageViewModelFactory(board!!, activity!!.application)
        pageViewModel = ViewModelProvider(this, factory)
            .get(NoticePageViewModel::class.java)

        initCustomTabs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notices, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = pageViewModel

        val noticesAdapter = NoticesAdapter(
            NoticeClickListener { pageViewModel.showNotice(it) },
            HeaderFooterClickListener { pageViewModel.expandBoard() })
        binding.noticesRecyclerView.adapter = noticesAdapter

        // Show notice with chrome custom tabs
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

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(board: Board): PlaceholderFragment =
            PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("board", board)
                }
            }
    }

    private fun initCustomTabs() {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(resources.getColor(R.color.colorPrimaryDark))
        customTabsIntent = builder.build()
    }
}
