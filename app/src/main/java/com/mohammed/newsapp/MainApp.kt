package com.mohammed.newsapp

import android.app.Application
import com.mohammed.newsapp.data.Repository
import com.mohammed.newsapp.network.Api
import com.mohammed.newsapp.network.NewsManager

class MainApp : Application() {

    private val manager by lazy { NewsManager(Api.retrofitService) }

    val repository by lazy { Repository(manager) }
}