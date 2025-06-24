package com.example.newsapp1.models

data class NewsResponse(
    val articles: MutableList<Article>?,
    val status: String?,
    val totalResults: Int?
)