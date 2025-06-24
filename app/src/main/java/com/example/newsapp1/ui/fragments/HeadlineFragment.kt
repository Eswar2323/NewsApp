package com.example.newsapp1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp1.R
import com.example.newsapp1.adapters.NewsAdapater
import com.example.newsapp1.databinding.FragmentHeadlineBinding
import com.example.newsapp1.ui.NewsActivity
import com.example.newsapp1.ui.NewsViewModel
import com.example.newsapp1.util.Constants.Companion.Qeury_Page_Size
import com.example.newsapp1.util.Resource

class HeadlineFragment : Fragment(R.layout.fragment_headline) {

    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapater: NewsAdapater
    lateinit var binding: FragmentHeadlineBinding

    val TAG = "HeadlineFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlineBinding.bind(view)
        setupRecycleView()
        newsAdapater.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_headlineFragment_to_articleFragment,
                bundle
            )
        }

        viewModel = (activity as NewsActivity).viewModel
        viewModel.getHeadlines("us")
        viewModel.headlinesNews.observe(viewLifecycleOwner, Observer{ response ->
            when(response){
                is Resource.Success -> {
                    hideProcessBar()
                    response.data?.let { newsResponse ->
                        newsAdapater.differ.submitList(newsResponse.articles?.toList())
                        val totalPages = newsResponse.totalResults?.div(Qeury_Page_Size+2)
                        isLastPage = viewModel.headlineNewPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProcessBar()
                    response.message?.let{ message ->
                       Toast.makeText(activity,"An Error occured $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProcessBar()
                }
            }
        })
    }

    var isLoading =false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBegging = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Qeury_Page_Size
            val shouldpaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBegging && isTotalMoreThanVisible

            if(shouldpaginate){
                viewModel.getHeadlines("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }
    }

    fun setupRecycleView(){
        newsAdapater = NewsAdapater()
        binding.recyclerHeadlines.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlineFragment.scrollListener)
        }
    }
    private fun hideProcessBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private  fun showProcessBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

}


