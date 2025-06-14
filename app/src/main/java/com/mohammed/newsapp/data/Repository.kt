package com.mohammed.newsapp.data

import com.mohammed.newsapp.network.NewsManager

class Repository(private val newsManager: NewsManager) {

    suspend fun getArticles() = newsManager.getArticles("us")

    suspend fun getArticlesByCategory(category: String) = newsManager.getArticlesByCategory(category)

    suspend fun getArticlesBySource(source:String) = newsManager.getArticlesBySource(source)

    suspend fun getSearchArticles(query:String) = newsManager.getSearchArticles(query)
}