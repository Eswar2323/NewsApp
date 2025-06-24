package com.example.newsapp1.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp1.NewsApplication
import com.example.newsapp1.models.Article
import com.example.newsapp1.models.NewsResponse
import com.example.newsapp1.repository.NewsRespository
import com.example.newsapp1.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app: Application,val newsRespository: NewsRespository): AndroidViewModel(app) {

    val headlinesNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlineNewPage = 1
    var headLineNewsResponse : NewsResponse? = null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewPage = 1
    var searchNewsResponse : NewsResponse? = null


    init {
        getHeadlines("us")
    }

    fun getHeadlines(countryCode : String) = viewModelScope.launch {
        safeHeadlineNewsCall(countryCode)
    }

    fun searchNews(query: String) = viewModelScope.launch {
        safeSearchNewsCall(query)
    }

    private fun handleHeadlinesNews(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                headlineNewPage++
                if(headLineNewsResponse == null){
                    headLineNewsResponse = resultResponse
                }else{
                    val oldArticles = headLineNewsResponse?.articles
                    val  newArticles = resultResponse.articles
                    newArticles?.let { oldArticles?.addAll(it) }
                }
                return Resource.Success(headLineNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNews(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    newArticles?.let { oldArticles?.addAll(it) }
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    fun saveNews(article: Article) = viewModelScope.launch {
        val exists = newsRespository.isArticleSaved(article.url.toString())
        if (!exists) {
            newsRespository.upsert(article)
            _toastMessage.value = "Added to favorites"
        } else {
            _toastMessage.value = "Already in favorites"
        }
    }

    fun getSavedNews() = newsRespository.getFavoriteArticles()

    fun getDeleteNews(article: Article) = viewModelScope.launch {
        newsRespository.delete(article)
    }

    private suspend fun safeHeadlineNewsCall(countryCode: String){
        headlinesNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRespository.getHeadlines(countryCode, headlineNewPage)
                headlinesNews.postValue(handleHeadlinesNews(response))
            }else{
                headlinesNews.postValue(Resource.Error("No Internet Conncetion"))
            }
        }catch(t : Throwable){
            when(t){
                is IOException -> headlinesNews.postValue(Resource.Error("Network Failure"))
                else -> headlinesNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(query: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRespository.searchForNews(query, searchNewPage)
                searchNews.postValue(handleSearchNews(response))
            }else{
                searchNews.postValue(Resource.Error("No Internet Conncetion"))
            }
        }catch(t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection() : Boolean {
        val connectyManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        val activiteNetwork  = connectyManager.activeNetwork ?: return false
        val capabilities = connectyManager.getNetworkCapabilities(activiteNetwork) ?: return false
        return when{
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}