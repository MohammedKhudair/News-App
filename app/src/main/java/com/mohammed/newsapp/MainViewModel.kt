package com.mohammed.newsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.newsapp.models.ArticleCategory
import com.mohammed.newsapp.models.TopNewsResponse
import com.mohammed.newsapp.models.getArticleCategory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<MainApp>().repository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse: StateFlow<TopNewsResponse>
        get() = _newsResponse

    private val _getArticleByCategory = MutableStateFlow(TopNewsResponse())
    val getArticleByCategory: StateFlow<TopNewsResponse>
        get() = _getArticleByCategory

    val sourceName = MutableStateFlow("abc-news")
    private val _getArticlesBySource = MutableStateFlow(TopNewsResponse())
    val getArticlesBySource: StateFlow<TopNewsResponse>
        get() = _getArticlesBySource

    val query = MutableStateFlow("")
    private val _searchedNewsResponse = MutableStateFlow(TopNewsResponse())
    val searchedNewsResponse: StateFlow<TopNewsResponse>
        get() = _searchedNewsResponse

    private val _selectedCategory: MutableStateFlow<ArticleCategory?> = MutableStateFlow(null)
    val selectedCategory: StateFlow<ArticleCategory?>
        get() = _selectedCategory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean>
        get() = _isError

    private val exceptionHandler = CoroutineExceptionHandler { _, error ->
        _isError.value = error is Exception
    }

    fun getTopArticles() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _newsResponse.value = repository.getArticles()
            _isLoading.value = false
        }

    }

    fun getArticlesByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _getArticleByCategory.value = repository.getArticlesByCategory(category)
            _isLoading.value = false
        }
    }

    fun getArticlesBySource() {
        _isLoading.value = true
        viewModelScope.launch((Dispatchers.IO + exceptionHandler)) {
            _getArticlesBySource.value = repository.getArticlesBySource(sourceName.value)
            _isLoading.value = false
        }
    }

    fun getSearchArticles(query: String) {
        _isLoading.value = true
        viewModelScope.launch((Dispatchers.IO)) {
            _searchedNewsResponse.value = repository.getSearchArticles(query)
            _isLoading.value = false
        }
    }


    fun onSelectedCategoryChanged(category: String) {
        val newsCategory = getArticleCategory(category)
        _selectedCategory.value = newsCategory
    }

}