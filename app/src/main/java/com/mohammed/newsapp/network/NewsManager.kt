package com.mohammed.newsapp.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mohammed.newsapp.models.ArticleCategory
import com.mohammed.newsapp.models.TopNewsResponse
import com.mohammed.newsapp.models.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsManager(private val service: NewsService) {

    suspend fun getArticles(country: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getTopArticles(country = country)
        }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesByCategory(category = category)
        }

    suspend fun getArticlesBySource(source:String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesBySource(source)
        }

    suspend fun getSearchArticles(query: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticles(query)
        }

}