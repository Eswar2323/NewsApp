package com.example.newsapp1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp1.R
import com.example.newsapp1.adapters.NewsAdapater
import com.example.newsapp1.databinding.FragmentFavoriteBinding
import com.example.newsapp1.models.Article
import com.example.newsapp1.ui.NewsActivity
import com.example.newsapp1.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapater : NewsAdapater
    lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoriteBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        setupRecycleView()
        newsAdapater.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_favoriteFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int
            ) {
                val position = viewHolder.adapterPosition
                val article = newsAdapater.differ.currentList[position]
                viewModel.getDeleteNews(article)
                Snackbar.make(view,"Succesfully Delete the News", Snackbar.LENGTH_LONG).apply {
                    setAction("undo") {
                        viewModel.saveNews(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer{ articles ->
            newsAdapater.differ.submitList(articles)
        })
    }

    fun setupRecycleView(){
        newsAdapater = NewsAdapater()
        binding.recyclerFavourites.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
        }
    }
}