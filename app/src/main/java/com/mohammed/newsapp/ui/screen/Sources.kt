package com.mohammed.newsapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohammed.newsapp.MainViewModel
import com.mohammed.newsapp.R
import com.mohammed.newsapp.components.ErrorUi
import com.mohammed.newsapp.components.LoadingUi
import com.mohammed.newsapp.models.TopNewsArticle

@Composable
fun Sources(
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    val items = listOf(
        "TechCrunch" to "techcrunch",
        "TalkSport" to "talksport",
        "Business Insider" to "business-insider",
        "Reuters" to "reuters",
        "Politico" to "politico",
        "TheVerge" to "the-verge"
    )

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "${viewModel.sourceName.collectAsState().value} Source") },
            actions = {
                var menuExpended by remember { mutableStateOf(false) }
                IconButton(onClick = { menuExpended = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }

                MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                    DropdownMenu(
                        expanded = menuExpended,
                        onDismissRequest = { menuExpended = false }) {
                        items.forEach {
                            DropdownMenuItem(onClick = {
                                viewModel.sourceName.value = it.second
                                viewModel.getArticlesBySource()
                                menuExpended = false
                            }, content = { Text(text = it.first) })
                        }
                    }
                }
            }
        )

    }) {

        when {
            isLoading.value -> {
                LoadingUi()
            }
            isError.value -> {
                ErrorUi()
            }
            else -> {
                viewModel.getArticlesBySource()
                val articles =
                    viewModel.getArticlesBySource.collectAsState().value.articles ?: listOf()
                SourceContent(articles = articles)
            }

        }

    }
}

@Composable
fun SourceContent(articles: List<TopNewsArticle>) {
    val uriHandler = LocalUriHandler.current

    LazyColumn {
        items(articles) { articles ->

            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(tag = "URL", annotation = articles.url ?: "openapi.org")
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.purple_500),
                        textDecoration = TextDecoration.Underline
                    )
                ) { append("Read full article here") }
                pop()
            }//annotatedString


            Card(
                backgroundColor = colorResource(id = R.color.purple_700),
                modifier = Modifier.padding(8.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .height(200.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        text = articles.title ?: "Not available",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = articles.description ?: "Not available",
                        maxLines = 3,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis
                    )

                    Card(backgroundColor = Color.White, elevation = 8.dp) {
                        ClickableText(text = annotatedString, modifier = Modifier.padding(6.dp),
                            onClick = {
                                annotatedString.getStringAnnotations(it, it).firstOrNull()
                                    ?.let { result ->
                                        if (result.tag == "URL") {
                                            uriHandler.openUri(result.item)
                                        }
                                    }
                            }
                        )
                    }// Card

                }
            }
        }
    }
}

// Create function that can repeat the given number of times

@Preview
@Composable
fun SourceContentPreview() {
    SourceContent(
        articles = listOf(
            TopNewsArticle(
                author = "CBSBoston.com Staff",
                title = "Principal Beaten Unconscious At Dorchester School; Classes Canceled Thursday - CBS BostonClasses Canceled Thursday - CBS Boston",
                description = "Principal Patricia Lampron and another employee were assaulted at Henderson Upper Campus during dismissal on Wednesday.",
                publishedAt = "2021-11-04T01:55:00Z"
            )
        )
    )
}
