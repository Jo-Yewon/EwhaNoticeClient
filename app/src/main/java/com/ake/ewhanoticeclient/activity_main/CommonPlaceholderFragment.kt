package com.ake.ewhanoticeclient.activity_main

import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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

class CommonPlaceholderFragment : Fragment(), MainActivity.CommonBoardFragment {
    // For common boards
    companion object {
        private const val commonUrl =
            "http://www.ewha.ac.kr/ewha/news/notice.do"
        const val DOWNLOAD_PERMISSION_REQ_CODE = 101

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
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.webView.reload()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setCommon(this)
    }

    override fun onStop() {
        (activity as MainActivity).setCommon(null)
        super.onStop()
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.INVISIBLE
    }

    private fun showError() {
        binding.error.visibility = View.VISIBLE
    }

    private fun initWebView() {
        showLoading()

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
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    hideLoading()
                }

                @TargetApi(Build.VERSION_CODES.M)
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    if (error!!.errorCode != ERROR_UNKNOWN) showError()
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    if (errorCode != -1) showError()

                }
            }

            setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
                try {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.apply {
                        setMimeType(mimeType)
                        addRequestHeader("User-Agent", userAgent)
                        setDescription("Downloading file")

                        val fileName = contentDisposition.replace("attachment; filename=", "")
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
                            context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity!!,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.download_permission_desc),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                DOWNLOAD_PERMISSION_REQ_CODE
                            )
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.download_permission_desc),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                DOWNLOAD_PERMISSION_REQ_CODE
                            )
                        }
                    }
                }
            }
            loadUrl(commonUrl)
        }
    }
}