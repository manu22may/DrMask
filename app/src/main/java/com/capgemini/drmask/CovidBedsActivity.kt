package com.capgemini.drmask

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_covid_beds.*


class CovidBedsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_beds)

        val url = intent.getStringExtra("url")?:"https://tncovidbeds.tnega.org/"
        webview.webViewClient = MyWebClient()
        webview.loadUrl(url)
        webview.settings.javaScriptEnabled =true
    }

    inner class MyWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

/*        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request!!.url.toString()
           if (url.startsWith("http:") || url.startsWith("https:")) {
                view?.loadUrl(url)
                return true
            }
            else return false
        }*/

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request!!.url.toString()
            if( URLUtil.isNetworkUrl(url) ) {
                view?.loadUrl(url)
                return false
            }
            else if(isAppInstalled(url))
            {
                view?.stopLoading()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
            else{
                view?.stopLoading()
                return true
            }
        }

        private fun isAppInstalled(uri: String): Boolean {
            val pm = packageManager
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
            }
            return false
        }
    }

    override fun onBackPressed() {
        if(webview.canGoBack())
            webview.goBack()
        else
            super.onBackPressed()
    }
}