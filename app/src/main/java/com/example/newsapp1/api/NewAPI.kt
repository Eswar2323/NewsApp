package com.example.newsapp1.api

import com.example.newsapp1.models.NewsResponse
import com.example.newsapp1.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apikey:  String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchNews : String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey:String = API_KEY
    ): Response<NewsResponse>
}