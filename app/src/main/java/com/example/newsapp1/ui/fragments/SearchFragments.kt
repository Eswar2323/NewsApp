package com.example.newsapp1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp1.R
import com.example.newsapp1.adapters.NewsAdapater
import com.example.newsapp1.databinding.FragmentSearchBinding
import com.example.newsapp1.ui.NewsActivity
import com.example.newsapp1.ui.NewsViewModel
import com.example.newsapp1.util.Constants.Companion.Search_News_Time_Dealy
import com.example.newsapp1.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragments : Fragment(R.layout.fragment_search) {
    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapater: NewsAdapater
    lateinit var binding: FragmentSearchBinding
    val TAG = "SearchFragments"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        binding = FragmentSearchBinding.bind(view)
        setupRecycleView()
        newsAdapater.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchFragments_to_articleFragment,
                bundle
            )
        }

        var job : Job? = null
        binding.searchEdit.addTextChangedListener{ editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Search_News_Time_Dealy)
            }
            editable?.let {
                if(editable.toString().isNotEmpty()){
                    viewModel.searchNews(editable.toString())
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer{ response ->
            when(response){
                is Resource.Success -> {
                    hideProcessBar()
                    response.data?.let { newsResponse ->
                        newsAdapater.differ.submitList(newsResponse.articles)
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


    fun setupRecycleView(){
        newsAdapater = NewsAdapater()
        binding.recyclerSearch.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun hideProcessBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private  fun showProcessBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}