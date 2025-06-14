package com.mohammed.newsapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohammed.newsapp.MainViewModel
import com.mohammed.newsapp.MockData
import com.mohammed.newsapp.MockData.getTimeAgo
import com.mohammed.newsapp.R
import com.mohammed.newsapp.components.ErrorUi
import com.mohammed.newsapp.components.LoadingUi
import com.mohammed.newsapp.components.SearchBar
import com.mohammed.newsapp.models.TopNewsArticle
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun TopNews(
    navController: NavController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(query = query, viewModel = viewModel)

        val searchText = query.value
        val searchResultList = mutableListOf<TopNewsArticle>()
        if (searchText != "") {
            searchResultList.addAll(
                viewModel.searchedNewsResponse.collectAsState().value.articles ?: articles
            )
        } else {
            searchResultList.addAll(articles)
        }


        when {
            isLoading.value -> {
                LoadingUi()
            }
            isError.value -> {
                ErrorUi()
            }
            else -> {
                LazyColumn {
                    items(searchResultList.size) { index ->
                        TopNewsItem(
                            article = searchResultList[index],
                            onNewsClick = { navController.navigate("Details/$index") })
                    }
                }
            }
        }


    }
}

@Composable
fun TopNewsItem(article: TopNewsArticle, onNewsClick: () -> Unit = {}) {

    Box(modifier = Modifier
        .height(200.dp)
        .padding(8.dp)
        .clickable { onNewsClick() })
    {
        CoilImage(
            imageModel = article.urlToImage,
            contentScale = ContentScale.Crop,
            error = ImageBitmap.imageResource(R.drawable.breaking_news),
            placeHolder = ImageBitmap.imageResource(id = R.drawable.breaking_news)
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 16.dp, start = 16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            article.publishedAt?.let {
                Text(
                    text = MockData.stringToDate(article.publishedAt)?.getTimeAgo()
                        ?: "Not available",
                    color = Color.White, fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(100.dp))

            article.title?.let {
                Text(text = article.title, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopNewsPreview() {
    TopNewsItem(
        TopNewsArticle(
            author = "Namita Singh",
            title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
            description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
            publishedAt = "2021-11-04T04:42:40Z"
        ),
        onNewsClick = {})
}