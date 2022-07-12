package com.lightfeather.wide_awakefinancials.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.lightfeather.wide_awakefinancials.R


class PrivacyPolicyFragment : Fragment() {



    private lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_privacy_policy, container, false)
        webView = root.findViewById(R.id.privacy_policy_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.setBackgroundColor(0x00000000);
        webView.isHorizontalScrollBarEnabled = false;
        webView.settings.javaScriptEnabled = true;
        webView.settings.useWideViewPort = true;
        webView.loadUrl("https://sites.google.com/view/wide-awake-financials/home")
    }


}