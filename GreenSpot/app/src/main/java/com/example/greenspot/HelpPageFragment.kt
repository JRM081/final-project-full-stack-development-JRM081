package com.example.greenspot

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.greenspot.databinding.FragmentHelpPageBinding

class HelpPageFragment : Fragment() {

    private val args: HelpPageFragmentArgs by navArgs()
    @SuppressLint("SetJavaScriptEnabled")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHelpPageBinding.inflate(
            inflater,
            container,
            false
        )
        binding.apply {
            webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(args.helpPageUri.toString())
            }
        }

        return binding.root
    }
}
