package com.example.newsapp1.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp1.repository.NewsRespository

class NewsViewModelProviderFactory(val app: Application,val newsRespository: NewsRespository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRespository) as T
    }
}