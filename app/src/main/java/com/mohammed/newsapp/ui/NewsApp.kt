package com.mohammed.newsapp.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohammed.newsapp.BottomMenuScreen
import com.mohammed.newsapp.MainViewModel
import com.mohammed.newsapp.components.BottomMenu
import com.mohammed.newsapp.models.TopNewsArticle
import com.mohammed.newsapp.network.Api
import com.mohammed.newsapp.network.NewsManager
import com.mohammed.newsapp.ui.screen.Categories
import com.mohammed.newsapp.ui.screen.DetailsScreen
import com.mohammed.newsapp.ui.screen.Sources
import com.mohammed.newsapp.ui.screen.TopNews

@Composable
fun NewsApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    MainScreen(navController, scrollState,viewModel = viewModel)
}

@Composable
fun MainScreen(
    navController: NavHostController,
    scrollState: ScrollState,
    viewModel: MainViewModel
) {
    Scaffold(bottomBar = { BottomMenu(navController = navController) })
    {
        Navigation(navController, scrollState, padding = it, viewModel = viewModel)
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    scrollState: ScrollState,
    padding: PaddingValues,
    viewModel: MainViewModel
) {
    val articlesList = mutableListOf(TopNewsArticle())
     val articles = viewModel.newsResponse.collectAsState().value.articles
      articlesList.addAll(articles ?: listOf())

    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()

    NavHost(
        navController = navController,
        startDestination = BottomMenuScreen.TopNews.route,
        modifier = Modifier.padding(padding)
    ) {

        val queryState = mutableStateOf(viewModel.query.value)
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)

        bottomNavigation(navController = navController,
            articles = articlesList,
            query = queryState,
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
        )

        composable(
            "Details/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            index?.let {
                if (queryState.value.isNotEmpty()) {
                    articlesList.clear()
                    articlesList.addAll(viewModel.searchedNewsResponse.value.articles ?: listOf())
                } else {
                    articlesList.clear()
                    articlesList.addAll(viewModel.newsResponse.value.articles ?: listOf())
                }

                val article = articlesList[index]
                DetailsScreen(
                    scrollState = scrollState,
                    article = article,
                    navController = navController
                )
            }

        }
    }


}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController,
            articles = articles,
            query = query,
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
        )
    }
    composable(BottomMenuScreen.Categories.route) {
        viewModel.onSelectedCategoryChanged("business")
        viewModel.getArticlesByCategory("business")

        Categories(
            viewModel = viewModel,
            onFetchCategory = {
                viewModel.onSelectedCategoryChanged(it)
                viewModel.getArticlesByCategory(it)
            },
            isLoading = isLoading,
            isError = isError
        )
    }

    composable(BottomMenuScreen.Sources.route) {
        Sources(
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
        )
    }
}