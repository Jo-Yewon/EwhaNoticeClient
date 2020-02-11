package com.ake.ewhanoticeclient.activity_main

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.databinding.FragmentCommonNoticesBinding

class CommonPlaceholderFragment : Fragment() {
    // For common boards
    companion object {
        private const val commonUrl =
            "http://www.ewha.ac.kr/mbs/ewhamk/jsp/board/list.jsp?boardId=13259&id=ewhamk_010502000000"

        @JvmStatic
        fun newInstance(): CommonPlaceholderFragment {
            return CommonPlaceholderFragment()
        }
    }

    private lateinit var binding: FragmentCommonNoticesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_common_notices, container, false
        )
        binding.lifecycleOwner = this

        initWebView()
        return binding.root
    }

    private fun showLoading(){
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.loading.visibility = View.VISIBLE
    }

    private fun showError(){
        binding.error.visibility = View.VISIBLE
    }

    private fun initWebView() {
        showLoading()

        binding.webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showError()
            }
        }

        val webSettings = binding.webView.settings
        webSettings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
        }

        binding.webView.apply {
            webViewClient = WebViewClient()
            setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
                try {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.apply {
                        setMimeType(mimeType)
                        addRequestHeader("User-Agent", userAgent)
                        setDescription("Downloading file")

                        var fileName = contentDisposition.replace("attachment; filename=", "");
                        fileName.apply {
                            replace("\"", "")
                            replace(";", "")
                        }

                        setTitle(fileName)
                        allowScanningByMediaScanner()
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    }

                    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)

                    Toast.makeText(context, "Downloading File", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    if (ContextCompat.checkSelfPermission(
                            context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity!!,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        {
                            Toast.makeText(context, "파일 다운로드를 위해 동의가 필요합니다.", Toast.LENGTH_LONG)
                                .show()
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                110)
                        }
                        else{
                            Toast.makeText(context, "파일 다운로드를 위해 동의가 필요합니다.", Toast.LENGTH_LONG)
                                .show()
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                110)
                        }
                    }
                }
            }
            loadUrl(commonUrl)
        }
    }
}