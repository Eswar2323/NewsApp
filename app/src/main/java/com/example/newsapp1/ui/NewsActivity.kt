package com.example.newsapp1.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp1.R
import com.example.newsapp1.databinding.ActivityNewsBinding
import com.example.newsapp1.db.ArticleDatabase
import com.example.newsapp1.repository.NewsRespository
import androidx.navigation.fragment.NavHostFragment

class NewsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)
        if (navHostFragment is NavHostFragment) {
            val navController = navHostFragment.navController
            binding.bottomNavigationView.setupWithNavController(navController)
        }

        val newsRespository = NewsRespository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRespository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    }
}