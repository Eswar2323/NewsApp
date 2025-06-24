package com.example.newsapp1.repository

import com.example.newsapp1.api.RetrofitInstance
import com.example.newsapp1.db.ArticleDatabase
import com.example.newsapp1.models.Article
import retrofit2.Retrofit
import java.util.Locale

class NewsRespository(val db : ArticleDatabase) {

    suspend fun getHeadlines(countryCode : String,pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode,pageNumber)

    suspend fun searchForNews(searchNews: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchNews,pageNumber)

    suspend fun upsert(article: Article) =
        db.getArticleDao().upsert(article)

    fun getFavoriteArticles() =
        db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) =
        db.getArticleDao().deleteArticle(article)

    suspend fun isArticleSaved(url: String): Boolean =
        db.getArticleDao().getArticleByUrl(url) != null



}