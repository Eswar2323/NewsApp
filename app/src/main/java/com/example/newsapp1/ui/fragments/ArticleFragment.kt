package com.example.newsapp1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp1.R
import com.example.newsapp1.databinding.FragmentArticleBinding
import com.example.newsapp1.ui.NewsActivity
import com.example.newsapp1.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    lateinit var binding: FragmentArticleBinding
    val args : ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url.toString())
        }

        binding.fab.setOnClickListener {
            viewModel.saveNews(article)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }

    }
}